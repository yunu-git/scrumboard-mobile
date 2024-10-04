package nz.ac.canterbury.seng303.scrumboardmobile.models

interface ScrumboardConstants {
    enum class Complexity(val complexity: String) {
        UNSET("Unset"),
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High")
    }

    enum class Status(val status: String) {
        TO_DO("To Do"),
        IN_PROGRESS("In Progress"),
        UNDER_REVIEW("Under Review"),
        DONE("Done")
    }

    enum class Priority(val priority: String) {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High"),
        CRITICAL("Critical"),
        UNSET("Unset")
    }
}