/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drive.student.xutils;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewUtils {

    private ViewUtils() {
    }

    public static void inject(View view) {
        injectObject(view, new com.drive.student.xutils.view.ViewFinder(view));
    }

    public static void inject(Activity activity) {
        injectObject(activity, new com.drive.student.xutils.view.ViewFinder(activity));
    }

    public static void inject(PreferenceActivity preferenceActivity) {
        injectObject(preferenceActivity, new com.drive.student.xutils.view.ViewFinder(preferenceActivity));
    }

    public static void inject(Object handler, View view) {
        injectObject(handler, new com.drive.student.xutils.view.ViewFinder(view));
    }

    public static void inject(Object handler, Activity activity) {
        injectObject(handler, new com.drive.student.xutils.view.ViewFinder(activity));
    }

    public static void inject(Object handler, PreferenceGroup preferenceGroup) {
        injectObject(handler, new com.drive.student.xutils.view.ViewFinder(preferenceGroup));
    }

    public static void inject(Object handler, PreferenceActivity preferenceActivity) {
        injectObject(handler, new com.drive.student.xutils.view.ViewFinder(preferenceActivity));
    }

    @SuppressWarnings("ConstantConditions")
    private static void injectObject(Object handler, com.drive.student.xutils.view.ViewFinder finder) {

        Class<?> handlerType = handler.getClass();

        // inject ContentView
        com.drive.student.xutils.view.annotation.ContentView contentView = handlerType.getAnnotation(com.drive.student.xutils.view.annotation.ContentView.class);
        if (contentView != null) {
            try {
                Method setContentViewMethod = handlerType.getMethod("setContentView", int.class);
                setContentViewMethod.invoke(handler, contentView.value());
            } catch (Throwable e) {
                com.drive.student.xutils.util.LogUtils.e(e.getMessage(), e);
            }
        }

        // inject view
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                com.drive.student.xutils.view.annotation.ViewInject viewInject = field.getAnnotation(com.drive.student.xutils.view.annotation.ViewInject.class);
                if (viewInject != null) {
                    try {
                        View view = finder.findViewById(viewInject.value(), viewInject.parentId());
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(handler, view);
                        }
                    } catch (Throwable e) {
                        com.drive.student.xutils.util.LogUtils.e(e.getMessage(), e);
                    }
                } else {
                    com.drive.student.xutils.view.annotation.ResInject resInject = field.getAnnotation(com.drive.student.xutils.view.annotation.ResInject.class);
                    if (resInject != null) {
                        try {
                            Object res = com.drive.student.xutils.view.ResLoader.loadRes(resInject.type(), finder.getContext(), resInject.id());
                            if (res != null) {
                                field.setAccessible(true);
                                field.set(handler, res);
                            }
                        } catch (Throwable e) {
                            com.drive.student.xutils.util.LogUtils.e(e.getMessage(), e);
                        }
                    } else {
                        com.drive.student.xutils.view.annotation.PreferenceInject preferenceInject = field.getAnnotation(com.drive.student.xutils.view.annotation.PreferenceInject.class);
                        if (preferenceInject != null) {
                            try {
                                Preference preference = finder.findPreference(preferenceInject.value());
                                if (preference != null) {
                                    field.setAccessible(true);
                                    field.set(handler, preference);
                                }
                            } catch (Throwable e) {
                                com.drive.student.xutils.util.LogUtils.e(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }

        // inject event
        Method[] methods = handlerType.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        Class<?> annType = annotation.annotationType();
                        if (annType.getAnnotation(com.drive.student.xutils.view.annotation.event.EventBase.class) != null) {
                            method.setAccessible(true);
                            try {
                                // ProGuard：-keep class * extends
                                // java.lang.annotation.Annotation { *; }
                                Method valueMethod = annType.getDeclaredMethod("value");
                                Method parentIdMethod = null;
                                try {
                                    parentIdMethod = annType.getDeclaredMethod("parentId");
                                } catch (Throwable e) {
                                }
                                Object values = valueMethod.invoke(annotation);
                                Object parentIds = parentIdMethod == null ? null : parentIdMethod.invoke(annotation);
                                int parentIdsLen = parentIds == null ? 0 : Array.getLength(parentIds);
                                int len = Array.getLength(values);
                                for (int i = 0; i < len; i++) {
                                    com.drive.student.xutils.view.ViewInjectInfo info = new com.drive.student.xutils.view.ViewInjectInfo();
                                    info.value = Array.get(values, i);
                                    info.parentId = parentIdsLen > i ? (Integer) Array.get(parentIds, i) : 0;
                                    com.drive.student.xutils.view.EventListenerManager.addEventMethod(finder, info, annotation, handler, method);
                                }
                            } catch (Throwable e) {
                                com.drive.student.xutils.util.LogUtils.e(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }
    }

}
