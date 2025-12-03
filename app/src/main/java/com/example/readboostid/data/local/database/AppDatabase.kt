// File: data/local/database/AppDatabase.kt
package com.readboost.id.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.readboost.id.data.local.dao.*
import com.readboost.id.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Article::class,
        Notes::class,
        UserProgress::class,
        Leaderboard::class,
        AdminUser::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun notesDao(): NotesDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun leaderboardDao(): LeaderboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "readboost_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val instance = INSTANCE
                if (instance != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            populateDatabase(instance)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        private suspend fun populateDatabase(database: AppDatabase) {
            val articleDao = database.articleDao()
            val userProgressDao = database.userProgressDao()
            val leaderboardDao = database.leaderboardDao()

            // Insert dummy articles
            val articles = listOf(
                Article(
                    title = "Memahami Kecerdasan Buatan (AI)",
                    content = """
Kecerdasan Buatan atau Artificial Intelligence (AI) adalah simulasi kecerdasan manusia yang diprogram dalam mesin. AI memungkinkan komputer untuk belajar dari pengalaman, menyesuaikan dengan input baru, dan melakukan tugas-tugas yang biasanya memerlukan kecerdasan manusia.

Jenis-jenis AI meliputi Machine Learning, Deep Learning, dan Neural Networks. Machine Learning adalah subset dari AI yang memungkinkan sistem untuk belajar dari data tanpa diprogram secara eksplisit. Deep Learning menggunakan jaringan neural yang dalam untuk memproses data dengan cara yang mirip dengan otak manusia.

Aplikasi AI dalam kehidupan sehari-hari sangat luas, mulai dari asisten virtual seperti Siri dan Google Assistant, sistem rekomendasi di Netflix dan Spotify, hingga mobil self-driving. AI juga digunakan dalam bidang kesehatan untuk diagnosis penyakit, dalam keuangan untuk deteksi fraud, dan dalam pendidikan untuk personalisasi pembelajaran.

Di Indonesia, pengembangan AI semakin pesat dengan munculnya berbagai startup teknologi dan dukungan pemerintah. Penting bagi kita untuk memahami AI agar dapat memanfaatkannya secara optimal dalam berbagai aspek kehidupan.
                    """.trimIndent(),
                    duration = 5,
                    category = "Teknologi",
                    difficulty = "Dasar",
                    xp = 15
                ),
                Article(
                    title = "Psikologi Kebahagiaan dalam Kehidupan Sehari-hari",
                    content = """
Kebahagiaan adalah kondisi emosional yang ditandai dengan perasaan positif seperti kepuasan, kegembiraan, dan makna hidup. Penelitian dalam psikologi positif menunjukkan bahwa kebahagiaan bukan hanya ditentukan oleh faktor eksternal seperti kekayaan atau status sosial.

Faktor-faktor yang mempengaruhi kebahagiaan meliputi hubungan sosial yang berkualitas, aktivitas yang bermakna, kesehatan fisik dan mental, serta praktik syukur. Studi menunjukkan bahwa 50% kebahagiaan kita ditentukan oleh genetika, 10% oleh keadaan hidup, dan 40% oleh tindakan yang kita pilih.

Cara meningkatkan kebahagiaan antara lain: praktik mindfulness, olahraga teratur, membangun hubungan sosial yang positif, menetapkan dan mencapai tujuan pribadi, serta berlatih syukur setiap hari. Penelitian juga menunjukkan bahwa membantu orang lain dapat meningkatkan kebahagiaan kita sendiri.

Penting untuk memahami bahwa kebahagiaan bukanlah tujuan akhir, melainkan perjalanan yang terus berlanjut. Dengan memahami faktor-faktor yang mempengaruhi kebahagiaan, kita dapat membuat keputusan yang lebih baik untuk meningkatkan kualitas hidup.
                    """.trimIndent(),
                    duration = 4,
                    category = "Psikologi",
                    difficulty = "Dasar",
                    xp = 12
                ),
                Article(
                    title = "Sejarah Internet: Dari ARPANET hingga Era Modern",
                    content = """
Internet dimulai sebagai proyek militer AS pada tahun 1960-an yang disebut ARPANET. Tujuannya adalah menciptakan jaringan komunikasi yang tahan terhadap serangan nuklir. Pada tahun 1983, ARPANET beralih ke protokol TCP/IP yang masih digunakan hingga saat ini.

World Wide Web diciptakan oleh Tim Berners-Lee pada tahun 1989 di CERN, Swiss. Ini berbeda dengan internet; Web adalah sistem informasi yang berjalan di atas internet. Browser web pertama yang populer adalah Mosaic pada tahun 1993, diikuti oleh Netscape Navigator.

Perkembangan internet sangat pesat sejak tahun 2000-an dengan munculnya Web 2.0, media sosial, cloud computing, dan Internet of Things (IoT). Saat ini, lebih dari 5 miliar orang di dunia menggunakan internet, mengubah cara kita bekerja, berkomunikasi, dan mengakses informasi.

Internet telah merevolusi hampir setiap aspek kehidupan modern, dari pendidikan hingga perdagangan, dari hiburan hingga kesehatan. Memahami sejarah internet membantu kita menghargai teknologi yang kita gunakan setiap hari.
                    """.trimIndent(),
                    duration = 6,
                    category = "Sejarah",
                    difficulty = "Menengah",
                    xp = 18
                ),
                Article(
                    title = "Fotosintesis: Proses Kehidupan Tumbuhan",
                    content = """
Fotosintesis adalah proses di mana tumbuhan mengubah energi cahaya matahari menjadi energi kimia dalam bentuk glukosa. Proses ini terjadi di kloroplas, khususnya di dalam pigmen hijau yang disebut klorofil.

Fotosintesis terdiri dari dua tahap utama: reaksi terang dan reaksi gelap (siklus Calvin). Reaksi terang terjadi di membran tilakoid, di mana energi cahaya digunakan untuk memecah molekul air dan menghasilkan ATP dan NADPH. Reaksi gelap terjadi di stroma, di mana CO2 diubah menjadi glukosa menggunakan ATP dan NADPH.

Persamaan sederhana fotosintesis: 6CO2 + 6H2O + energi cahaya → C6H12O6 + 6O2. Proses ini sangat penting bagi kehidupan di Bumi karena menghasilkan oksigen yang kita hirup dan menjadi dasar rantai makanan.

Tanpa fotosintesis, kehidupan seperti yang kita kenal tidak akan ada. Proses ini tidak hanya penting untuk tumbuhan, tetapi juga untuk seluruh ekosistem dan kelangsungan hidup makhluk hidup lainnya.
                    """.trimIndent(),
                    duration = 7,
                    category = "Sains",
                    difficulty = "Menengah",
                    xp = 20
                ),
                Article(
                    title = "Manajemen Waktu untuk Produktivitas Maksimal",
                    content = """
Manajemen waktu adalah keterampilan penting dalam mencapai kesuksesan pribadi dan profesional. Dengan mengelola waktu dengan baik, kita dapat menyelesaikan lebih banyak tugas, mengurangi stres, dan memiliki lebih banyak waktu untuk hal-hal yang penting.

Teknik-teknik manajemen waktu yang efektif meliputi: metode Pomodoro (bekerja 25 menit, istirahat 5 menit), prioritas tugas dengan matriks Eisenhower, dan teknik time blocking. Penting juga untuk menghindari prokrastinasi dengan memecah tugas besar menjadi langkah-langkah kecil.

Kunci sukses manajemen waktu adalah konsistensi dan disiplin. Mulailah dengan membuat jadwal harian, tetapkan tujuan yang realistis, dan evaluasi progres secara berkala. Ingat bahwa manajemen waktu bukan tentang mengisi setiap menit dengan pekerjaan, tetapi tentang menggunakan waktu secara bijak.

Dengan menguasai manajemen waktu, kita dapat mencapai keseimbangan antara pekerjaan dan kehidupan pribadi, meningkatkan produktivitas, dan mengurangi stres dalam kehidupan sehari-hari.
                    """.trimIndent(),
                    duration = 3,
                    category = "Motivasi",
                    difficulty = "Dasar",
                    xp = 10
                ),
                Article(
                    title = "Blockchain: Teknologi di Balik Cryptocurrency",
                    content = """
Blockchain adalah teknologi distributed ledger yang mencatat transaksi secara aman dan transparan. Setiap blok dalam blockchain berisi data transaksi, timestamp, dan hash dari blok sebelumnya, membentuk rantai yang tidak dapat diubah.

Karakteristik utama blockchain meliputi: desentralisasi (tidak ada otoritas pusat), transparansi (semua transaksi dapat dilihat), immutability (data tidak dapat diubah setelah dicatat), dan keamanan (menggunakan kriptografi). Bitcoin adalah aplikasi blockchain yang paling terkenal, tetapi teknologi ini memiliki potensi di banyak bidang lain.

Aplikasi blockchain di luar cryptocurrency meliputi: supply chain management, voting elektronik, smart contracts, manajemen identitas digital, dan rekam medis. Di Indonesia, beberapa perusahaan dan pemerintah mulai mengeksplorasi penggunaan blockchain untuk berbagai aplikasi.

Memahami blockchain penting karena teknologi ini berpotensi mengubah cara kita bertransaksi, berbagi informasi, dan membangun kepercayaan dalam sistem digital. Meskipun masih dalam tahap perkembangan, blockchain menawarkan solusi untuk banyak masalah dalam dunia digital.
                    """.trimIndent(),
                    duration = 8,
                    category = "Teknologi",
                    difficulty = "Lanjut",
                    xp = 25
                ),
                Article(
                    title = "Budaya Gotong Royong di Indonesia",
                    content = """
Gotong royong adalah nilai budaya Indonesia yang mencerminkan semangat kebersamaan dan saling membantu dalam masyarakat. Konsep ini telah menjadi bagian integral dari kehidupan sosial masyarakat Indonesia sejak zaman dahulu.

Praktik gotong royong dapat ditemukan dalam berbagai aspek kehidupan, mulai dari membangun rumah, membersihkan lingkungan, hingga merayakan acara-acara penting. Di era modern, semangat gotong royong tetap relevan dan penting untuk memperkuat solidaritas sosial.

Nilai-nilai gotong royong meliputi: kerelawanan, kebersamaan, tanggung jawab bersama, dan kepedulian terhadap sesama. Dalam konteks pembangunan nasional, gotong royong menjadi modal sosial yang penting untuk mencapai tujuan bersama.

Melestarikan budaya gotong royong di era digital memerlukan adaptasi dan kreativitas. Misalnya, melalui platform digital untuk menggalang dana, volunteer matching, atau kampanye sosial. Dengan menjaga semangat gotong royong, kita dapat membangun masyarakat yang lebih solid dan harmonis.
                    """.trimIndent(),
                    duration = 4,
                    category = "Sosial & Budaya",
                    difficulty = "Dasar",
                    xp = 12
                ),
                Article(
                    title = "Perubahan Iklim dan Dampaknya",
                    content = """
Perubahan iklim adalah perubahan jangka panjang dalam pola cuaca dan suhu rata-rata di Bumi. Penyebab utama perubahan iklim adalah aktivitas manusia, terutama pembakaran bahan bakar fosil yang menghasilkan gas rumah kaca seperti CO2.

Dampak perubahan iklim sudah terasa di berbagai belahan dunia: kenaikan suhu global, pencairan es di kutub, kenaikan permukaan laut, perubahan pola curah hujan, dan peningkatan frekuensi cuaca ekstrem. Di Indonesia, dampaknya termasuk banjir, kekeringan, dan ancaman terhadap biodiversitas.

Mitigasi perubahan iklim memerlukan aksi dari semua pihak: pemerintah, industri, dan individu. Langkah-langkah yang dapat dilakukan meliputi: mengurangi emisi karbon, menggunakan energi terbarukan, menanam pohon, mengurangi konsumsi daging, dan memilih transportasi ramah lingkungan.

Adaptasi terhadap perubahan iklim juga penting, termasuk membangun infrastruktur yang tahan bencana, mengembangkan varietas tanaman yang tahan kekeringan, dan meningkatkan kesadaran masyarakat. Setiap individu memiliki peran dalam mengatasi perubahan iklim untuk masa depan yang lebih baik.
                    """.trimIndent(),
                    duration = 6,
                    category = "Sains",
                    difficulty = "Menengah",
                    xp = 18
                )
            )

            articles.forEach { articleDao.insertArticle(it) }

            // Insert initial user progress
            val userProgress = UserProgress(
                id = 1,
                totalXP = 0,
                streakDays = 0,
                dailyTarget = 5,
                lastReadDate = 0L,
                totalReadingTime = 0
            )
            userProgressDao.insertUserProgress(userProgress)

            // Insert dummy leaderboard data
            val leaderboards = listOf(
                Leaderboard(username = "Budi Santoso", totalXP = 850, rank = 1),
                Leaderboard(username = "Siti Nurhaliza", totalXP = 720, rank = 2),
                Leaderboard(username = "Ahmad Fauzi", totalXP = 680, rank = 3),
                Leaderboard(username = "Rina Wijaya", totalXP = 550, rank = 4),
                Leaderboard(username = "Dedi Kurniawan", totalXP = 490, rank = 5),
                Leaderboard(username = "Maya Puspita", totalXP = 430, rank = 6),
                Leaderboard(username = "Eko Prasetyo", totalXP = 380, rank = 7),
                Leaderboard(username = "Indah Permata", totalXP = 320, rank = 8),
                Leaderboard(username = "Hendra Wijaya", totalXP = 280, rank = 9),
                Leaderboard(username = "Fitri Handayani", totalXP = 250, rank = 10)
            )
            leaderboardDao.insertAllLeaderboard(leaderboards)
        }
    }
}