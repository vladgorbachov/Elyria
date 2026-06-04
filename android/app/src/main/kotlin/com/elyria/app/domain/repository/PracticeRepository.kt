package com.elyria.app.domain.repository

import com.elyria.app.domain.model.Practice

interface PracticeRepository {
    fun getAll(): List<Practice>

    fun getById(id: String): Practice?
}
