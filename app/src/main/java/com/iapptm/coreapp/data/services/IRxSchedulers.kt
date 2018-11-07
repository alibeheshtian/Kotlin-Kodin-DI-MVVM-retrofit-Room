package com.iapptm.coreapp.data.services

import io.reactivex.Scheduler

interface IRxSchedulers {
  fun main(): Scheduler
  fun io(): Scheduler
}