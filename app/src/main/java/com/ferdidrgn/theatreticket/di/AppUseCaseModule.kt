package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.domain.useCase.contactUsEmail.GetContactUsEmailUseCase
import com.ferdidrgn.theatreticket.domain.useCase.contactUsEmail.GetContactUsEmailUseCaseImpl
import com.ferdidrgn.theatreticket.domain.useCase.termsConditionPrivacyPolicy.GetTermsConditionOrPrivacyPolicyUseCase
import com.ferdidrgn.theatreticket.domain.useCase.termsConditionPrivacyPolicy.GetTermsConditionOrPrivacyPolicyUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppUseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetContactUsEmailUseCase(getContactUsEmailUseCaseImpl: GetContactUsEmailUseCaseImpl): GetContactUsEmailUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetTermsConditionOrPrivacyPolicyUseCase(
        getTermsConditionOrPrivacyPolicyUseCaseImpl: GetTermsConditionOrPrivacyPolicyUseCaseImpl
    ): GetTermsConditionOrPrivacyPolicyUseCase
}