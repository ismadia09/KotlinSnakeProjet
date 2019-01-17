package data.model

import java.io.Serializable
import java.time.LocalDateTime

class GameSave(val save: MutableList<SaveItem>, val score: Int, val saveDate: LocalDateTime) : Serializable