package com.mindease.app.data.repository

import com.mindease.app.data.local.practice.PracticeCatalog
import com.mindease.app.domain.model.Practice
import com.mindease.app.domain.repository.PracticeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PracticeRepositoryImpl @Inject constructor() : PracticeRepository {
    override fun getAll(): List<Practice> = PracticeCatalog.all
}
