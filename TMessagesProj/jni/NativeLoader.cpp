#include <jni.h>
#include <string>

/*static google_breakpad::ExceptionHandler *exceptionHandler;

bool callback(const google_breakpad::MinidumpDescriptor &descriptor, void *context, bool succeeded) {
    printf("dump path: %s\n", descriptor.path());
    return succeeded;
}*/

extern "C" {
void Java_ir_amin_HaftTeen_NativeLoader_init(JNIEnv *env, jobject obj, jstring filepath,
                                                        jboolean enable) {
    return;
    /*if (enable) {
        const char *path = env->GetStringUTFChars(filepath, 0);
        google_breakpad::MinidumpDescriptor descriptor(path);
        exceptionHandler = new google_breakpad::ExceptionHandler(descriptor, NULL, callback, NULL, true, -1);
    }*/
}


extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getBaseUrl(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "https://40star.vas24.ir/";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getSecureKey(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "25c6c7ff35a5979b461b2136cd13b0ff";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getReportKey(
        JNIEnv *env,
        jobject /* this */) {
    //ea566cfd66c7e4d2daa19f4a003491a15168ae85 app1
    //3822c1e2b8025b62a0a0f16ceab9f29e8b6e348a app2
    //4695bdccbce2980c7a2c89c64e22f6bbef62b4dc app3
    //c1e7d67643887a040068b0483be56d6649483395 app4
    //507ae041380c222c6f7ce4d7902cdc6c9cf8ffe5 app5
    //4bf1fee198779c4e913a620ccdf392d22373cd3e base App
    std::string hello = "4bf1fee198779c4e913a620ccdf392d22373cd3e";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getReportUrl(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "http://countly2.shadianeh.ir";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getProjectId(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "1027366882413";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_org_telegram_messenger_NativeLoader_getDownloadLink(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "http://ha.4321.ir/aan.apk";
    return env->NewStringUTF(hello.c_str());
}

}