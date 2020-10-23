package edu.bluejack20_1.gogames.rawg.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class GameScreenScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class DetailScreenScope