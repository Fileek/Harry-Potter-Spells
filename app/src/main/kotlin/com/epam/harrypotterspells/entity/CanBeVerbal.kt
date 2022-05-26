package com.epam.harrypotterspells.entity

enum class CanBeVerbal {
    YES {
        override fun toString() = "Yes"
    },
    NO {
        override fun toString() = "No"
    },
    UNKNOWN {
        override fun toString() = "Unknown"
    }
}