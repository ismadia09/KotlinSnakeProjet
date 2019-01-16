package data.model

import module.Direction
import java.io.Serializable

class SaveItem(var direction: Direction,var playground : Array<Array<String>>) : Serializable{

}