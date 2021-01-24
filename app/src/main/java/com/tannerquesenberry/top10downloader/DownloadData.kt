package com.tannerquesenberry.top10downloader

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "DownloadData"

class DownloadData(private val callback: DownloaderCallBack) : AsyncTask<String, Void, String>() {

    interface DownloaderCallBack {
        fun onDataAvailable(data: List<FeedEntry>)
    }

    override fun doInBackground(vararg params: String): String {
        Log.d(TAG, "doInBackground: starts with ${params[0]}")
        val rssFeed = downloadXML(params[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")
        }
        return rssFeed
    }

    override fun onPostExecute(result: String) {
        val parseApplications = ParseApplications()
        if (result.isNotEmpty()) {
            parseApplications.parse(result)
        }

        callback.onDataAvailable(parseApplications.applications)
    }

    private fun downloadXML(urlPath: String): String {
        try {
            return URL(urlPath).readText()
        }catch(e: MalformedURLException) {
            Log.d(TAG, "downloadXML: Invalid URL " + e.message)
        }catch (e: IOException) {
            Log.d(TAG, "downloadWML: IO Exception reading data " + e.message)
        }catch (e: SecurityException) {
            Log.d(TAG, "downloadXML: Security exception Needs permission? " + e.message)
//            e.printStackTrace()
        }

        // If there was an exception
        return ""
    }

}
