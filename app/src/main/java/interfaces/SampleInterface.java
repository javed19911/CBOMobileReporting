package interfaces;

import android.content.Context;
import android.location.Location;

import utils.MyConnection;

/**
 * Created by Akshit on 4/14/2016.
 */
public class SampleInterface  {


    M_Internace m_internace;
    Context context ;
    MyConnection myCon;

    public SampleInterface(M_Internace m_internace){
        this.m_internace = m_internace;
        this.context = (Context) m_internace;
       awesomeExample();
    }

    public void awesomeExample(){

        m_internace.myLocationData("Hi i Did this i Won........");

    }


 }

