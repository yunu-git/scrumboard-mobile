package nz.ac.canterbury.seng303.scrumboardmobile.models

interface ScrumboardConstants {
    enum class Complexity {
        UNSET,
        LOW,
        MEDIUM,
        HIGH
    }

    enum class Status {
        TO_DO,
        IN_PROGRESS,
        UNDER_REVIEW,
        DONE
    }

    enum class Priority {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }

}