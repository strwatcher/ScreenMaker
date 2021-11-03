package com.strwatcher.screenmaker.constants

import java.io.File

class Paths {
    companion object {
        val home = System.getProperty("user.home")
        val pictures = home + File.separator + "Pictures"
    }
}