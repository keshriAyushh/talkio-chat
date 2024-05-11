package com.ayush.talkio.utils


sealed class Route(val route: String) {

    data object AUTH: Route("auth")
    data object MAIN: Route("main")

    data object SplashScreen: Route("splash")
    data object OnboardingScreen: Route("onboarding")
    data object LoginScreen: Route("login")
    data object VerifyCodeScreen: Route("verify_code")
    data object CompleteProfileScreen: Route("complete_profile")
    data object ChatScreen: Route("chat")
    data object AllChatsScreen: Route("all_chats")
    data object ProfileScreen: Route("profile")
}