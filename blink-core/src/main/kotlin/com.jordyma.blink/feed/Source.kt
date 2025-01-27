package com.jordyma.blink.feed

enum class Source(val source: String, val image: String){

    NAVER_BLOG("NAVER_BLOG", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%82%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%87%E1%85%A5-%E1%84%87%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3-%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    VELOG("VELOG", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%87%E1%85%A6%E1%86%AF%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3-%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    BRUNCH("BRUNCH", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%87%E1%85%B3%E1%84%85%E1%85%A5%E1%86%AB%E1%84%8E%E1%85%B5+%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    YOZM_IT("YOZM_IT", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%8B%E1%85%AD%E1%84%8C%E1%85%B3%E1%86%B7-it-%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    TISTORY("TISTORY", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%90%E1%85%B5%E1%84%89%E1%85%B3%E1%84%90%E1%85%A9%E1%84%85%E1%85%B5-%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    EO("EO", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/EO+%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.png"),
    NAVER("NAVER", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/naver.png"),
    YOUTUBE("YOUTUBE", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/youtube.png"),
    GOOGLE("GOOGLE", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/google.png"),
    DEFAULT("DEFAULT", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%83%E1%85%B5%E1%84%91%E1%85%A9%E1%86%AF%E1%84%90%E1%85%B3.png"),
    ONBOARDING("블링크", "https://jordyma-dev.s3.ap-northeast-2.amazonaws.com/brunch/%E1%84%83%E1%85%B5%E1%84%91%E1%85%A9%E1%86%AF%E1%84%90%E1%85%B3.png");

    companion object {
        fun getBrunchByImage(imageUrl: String): String? {
            return values().firstOrNull { it.image == imageUrl }?.source
        }

        fun getBrunchByName(brunchName: String): Source? {
            return values().firstOrNull { it.source == brunchName }
        }

        fun getImageByName(brunchName: String): String? {
            return values().firstOrNull { it.source == brunchName }!!.image
        }
    }
}
