package dev.sanskar.featuretesteduco.core.domain.util

interface Downloader {



    fun downloadFile(url: String): Long
}