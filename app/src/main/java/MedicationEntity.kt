import com.example.medication.core.database.entities.MedicationEntity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)