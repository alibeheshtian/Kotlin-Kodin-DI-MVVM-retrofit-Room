package com.shenoto.domain.interfaces

import io.reactivex.Scheduler

interface IRxSchedulers {
  fun main(): Scheduler
  fun io(): Scheduler
}