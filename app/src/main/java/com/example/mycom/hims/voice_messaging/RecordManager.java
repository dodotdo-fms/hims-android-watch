// YJ ADD

package com.example.mycom.hims.voice_messaging;

import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.mycom.hims.R;

public class RecordManager
{
    static public String filePath;

    static {
        try {
            filePath = new FileUtil().mkdir("loup");
        } catch (Exception e) {
            filePath = "/";
            e.printStackTrace();
        }
    }

    private MediaRecorder recorder = null;
    private boolean isRecorded = false;

    public void start(String fileName)
    {

        Log.e("file",fileName);
        isRecorded = true;
        if (recorder == null) recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filePath + fileName);
        try
        {
            recorder.prepare();
        }
        catch (Exception e)
        {
            Log.e("prepare","aa");
            e.printStackTrace();
            isRecorded = false;
        }
        recorder.start();
    }

    public void stop()
    {
        isRecorded = false;
        if (recorder == null) return;
        try
        {
            recorder.stop();
        }
        catch(Exception e){Log.e("prepare","aa2");}
        finally
        {
            recorder.release();
            recorder = null;
        }
    }

    public boolean isRecorded()
    {
        return isRecorded;
    }

    public void setRecorded(boolean recorded)
    {
        recorded = recorded;
    }
}
