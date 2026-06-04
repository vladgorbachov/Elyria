package com.mindease.app.domain.repository

import com.mindease.app.domain.model.Practice

interface PracticeRepository {
    fun getAll(): List<Practice>
}
