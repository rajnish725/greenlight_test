package com.fycus.di

import com.fycus.ui.activity.*
import com.fycus.ui.activity.forgotpassword.*
import com.fycus.ui.fragment.*
import com.fycus.ui.fragment.addCards.*
import com.fycus.ui.fragment.favourite.*
import com.fycus.ui.fragment.myCards.*
import com.fycus.ui.fragment.profile.*
import com.fycus.ui.fragment.shareCards.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Binds all sub-components within the app.
 */

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun SplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun LoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun ForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector
    abstract fun SignUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun OtpVerificationActivity(): OtpVerificationActivity

    @ContributesAndroidInjector
    abstract fun UpdatePasswordActivity(): UpdatePasswordActivity

    @ContributesAndroidInjector
    abstract fun HomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun HomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun ProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun EditProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun AllCardsFragment(): AllCardsFragment

    @ContributesAndroidInjector
    abstract fun NotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun ViewCardOrientationFragment(): ViewCardOrientationFragment

    @ContributesAndroidInjector
    abstract fun MyContactsFragment(): MyContactsFragment

    @ContributesAndroidInjector
    abstract fun MyVisitingCardsFragment(): MyVisitingCardsFragment

    @ContributesAndroidInjector
    abstract fun ViewMyVisitingCardFragment(): ViewMyVisitingCardFragment

    @ContributesAndroidInjector
    abstract fun CardShareActivity(): CardShareActivity

    @ContributesAndroidInjector
    abstract fun ShareCardsFragment(): ShareCardsFragment

    @ContributesAndroidInjector
    abstract fun ShareCardDetailsFragment(): ShareCardDetailsFragment

    @ContributesAndroidInjector
    abstract fun PurchaseCardsFragment(): PurchaseCardsFragment

    @ContributesAndroidInjector
    abstract fun SubscriptionPlanFragment(): SubscriptionPlanFragment

    @ContributesAndroidInjector
    abstract fun FaqFragment(): FaqFragment

    @ContributesAndroidInjector
    abstract fun HelpFragment(): HelpFragment

    @ContributesAndroidInjector
    abstract fun SearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun MyFavouriteCardsFragment(): MyFavouriteCardsFragment

    @ContributesAndroidInjector
    abstract fun ViewFavouriteCardsFragment(): ViewFavouriteCardsFragment

    @ContributesAndroidInjector
    abstract fun AddContactsFamilyBusinessAndFriendActivity(): AddContactsFamilyBusinessAndFriendActivity

}