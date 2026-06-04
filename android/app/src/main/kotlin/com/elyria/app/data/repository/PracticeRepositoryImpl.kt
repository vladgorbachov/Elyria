package com.elyria.app.data.repository

import com.elyria.app.data.local.practice.PracticeCatalog
import com.elyria.app.domain.model.Practice
import com.elyria.app.domain.repository.PracticeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PracticeRepositoryImpl @Inject constructor() : PracticeRepository {
    override fun getAll(): List<Practice> = PracticeCatalog.all

    override fun getById(id: String): Practice? = PracticeCatalog.all.find { it.id == id }
}
