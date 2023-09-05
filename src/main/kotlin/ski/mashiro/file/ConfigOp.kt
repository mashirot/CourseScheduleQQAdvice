package ski.mashiro.file

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.io.FileUtils
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.data.UserData
import ski.mashiro.entity.Config
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigOp {

    private val logger = CourseScheduleQQAdvice.logger
    private val CONFIG_FOLDER = CourseScheduleQQAdvice.configFolder
    private val CONFIG_FILE = File(CONFIG_FOLDER, "config.yml")
    var config = Config()

    private val yamlMapper = YAMLMapper().registerKotlinModule().registerModule(JavaTimeModule())

    fun saveConfig() {
        config.userMap = UserData.userMap
        if (!CONFIG_FILE.exists()) {
            initConfig()
            return
        }
        obj2File()
    }

    fun loadConfig() {
        if (!CONFIG_FILE.exists()) {
            initConfig()
            return
        }
        file2Obj()
        UserData.userMap.putAll(config.userMap)
    }

    private fun initConfig() {
        try {
            CONFIG_FILE.createNewFile()
            obj2File()
        } catch (e: Exception) {
            logger.error("Init config.yml failed, err msg: ${e.message}", e)
        }
    }

    private fun obj2File() {
        try {
            val cfgYAML = yamlMapper.writeValueAsString(config)
            FileUtils.writeStringToFile(CONFIG_FILE, cfgYAML, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            logger.error("Serialize config.yml failed, err msg: ${e.message}", e)
        }
    }

    private fun file2Obj() {
        try {
            val cfgYAML = FileUtils.readFileToString(CONFIG_FILE, StandardCharsets.UTF_8)
            config = yamlMapper.readValue(cfgYAML, Config::class.java)
        } catch (e: Exception) {
            logger.error("Deserialize config.yml failed, err msg: ${e.message}", e)
        }
    }

}