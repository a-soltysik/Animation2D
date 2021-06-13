package com.github.a_soltysik.animation2d.gui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Insets
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.*
import javax.swing.*
import javax.swing.text.Document
import javax.swing.text.StyleConstants

class LoggerPanel : JRootPane() {
    private val logPane: JTextPane = JTextPane()
    val logger: Logger = Logger(this)
    private var autoScroll = true
    private var paused = false
    private val maxSizeForEach = 2000
    private var currentMode = LogType.ALL
    private val logs = mapOf<LogType, LinkedList<String>>(
        Pair(LogType.INFO, LinkedList()),
        Pair(LogType.DEBUG, LinkedList()),
        Pair(LogType.WARN, LinkedList()),
        Pair(LogType.ERROR, LinkedList())
    )
    private val allLogs = LinkedList<Pair<LogType, String>>()

    init {
        val menuBar = JMenuBar()
        val menu = JMenu("Options")
        val autoScrollCheckbox = JCheckBoxMenuItem("Auto-scroll", autoScroll)
        val pauseCheckBox = JCheckBoxMenuItem("Pause logger", paused)
        val modeGroup = ButtonGroup()
        val allRadioButton = JRadioButtonMenuItem("All", true)
        val infoRadioButton = JRadioButtonMenuItem("Info", false)
        val debugRadioButton = JRadioButtonMenuItem("Debug", false)
        val warnRadioButton = JRadioButtonMenuItem("Warn", false)
        val errorRadioButton = JRadioButtonMenuItem("Error", false)
        allRadioButton.addActionListener { changeMode(LogType.ALL) }
        infoRadioButton.addActionListener { changeMode(LogType.INFO)}
        debugRadioButton.addActionListener { changeMode(LogType.DEBUG)}
        warnRadioButton.addActionListener { changeMode(LogType.WARN) }
        errorRadioButton.addActionListener { changeMode(LogType.ERROR) }
        modeGroup.add(allRadioButton)
        modeGroup.add(infoRadioButton)
        modeGroup.add(debugRadioButton)
        modeGroup.add(warnRadioButton)
        modeGroup.add(errorRadioButton)
        menu.add(allRadioButton)
        menu.add(infoRadioButton)
        menu.add(debugRadioButton)
        menu.add(warnRadioButton)
        menu.add(errorRadioButton)
        menu.addSeparator()
        menu.add(autoScrollCheckbox)
        menu.add(pauseCheckBox)
        menuBar.add(menu)
        autoScrollCheckbox.addActionListener { autoScroll = autoScrollCheckbox.isSelected }
        pauseCheckBox.addActionListener { paused = pauseCheckBox.isSelected }

        layout = BorderLayout()
        background = Color.darkGray
        logPane.background = Color.darkGray
        logPane.margin = Insets(5, 10, 5, 10)
        val scrollPane = JScrollPane(logPane)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        add(BorderLayout.CENTER, scrollPane)
        add(BorderLayout.NORTH, menuBar)

        val styles = mapOf(
            Pair(LogType.INFO, logPane.addStyle(LogType.INFO.tag, null)),
            Pair(LogType.DEBUG, logPane.addStyle(LogType.DEBUG.tag, null)),
            Pair(LogType.WARN, logPane.addStyle(LogType.WARN.tag, null)),
            Pair(LogType.ERROR, logPane.addStyle(LogType.ERROR.tag, null))
        )

        StyleConstants.setForeground(styles[LogType.INFO], Color.gray)
        StyleConstants.setForeground(styles[LogType.DEBUG], Color.lightGray)
        StyleConstants.setForeground(styles[LogType.WARN], Color.orange)
        StyleConstants.setForeground(styles[LogType.ERROR], Color.red)

        for (pair in styles) {
            StyleConstants.setFontFamily(pair.value, "Cascadia Code PL")
            StyleConstants.setFontSize(pair.value, 18)
        }

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                menuBar.size = Dimension(size.width, menu.preferredSize.height)
                scrollPane.size = Dimension(size.width, size.height - menu.height)
            }
        })
    }

    private inline fun print(log: String, logType: LogType, document: Document) {
        document.insertString(document.length, "${logType.tag}: $log\n", logPane.getStyle(logType.tag))

    }

    internal fun add(log: String, logType: LogType) {
        allLogs.addLast(Pair(logType, log))
        if (allLogs.size >= maxSizeForEach * 4) {
            allLogs.removeFirst()
        }
        logs[logType]?.add(log)
        if (logs[logType]!!.size >= maxSizeForEach) {
            logs[logType]!!.removeFirst()
        }
        if (!paused) {
            val document = logPane.document
            if (currentMode == LogType.ALL) {
                print(log, logType, document)
            } else if (logType == currentMode) {
                print(log, logType, document)
            }

            if (autoScroll)
                logPane.caretPosition = document.length - 1
        }
    }

    private fun changeMode(newMode: LogType) {
        if (newMode == currentMode) {
            return
        }
        paused = true
        logPane.text = ""
        val document = logPane.document;
        if (newMode == LogType.ALL) {
            for (log in allLogs) {
                document.insertString(
                    document.length,
                    "${log.first.tag}: ${log.second}\n",
                    logPane.getStyle(log.first.tag)
                )
            }
        } else {
            for (log in logs[newMode]!!) {
                print(log, newMode, document)
            }
        }
        currentMode = newMode
        paused = false
    }
}

class Logger(private val parent: LoggerPanel) {
    fun info(log: String) = parent.add(log, LogType.INFO)
    fun debug(log: String) = parent.add(log, LogType.DEBUG)
    fun warn(log: String) = parent.add(log, LogType.WARN)
    fun error(log: String) = parent.add(log, LogType.ERROR)
}

internal enum class LogType(val tag: String) {
    INFO("Info"), DEBUG("Debug"), WARN("Warn"), ERROR("Error"), ALL("All")
}