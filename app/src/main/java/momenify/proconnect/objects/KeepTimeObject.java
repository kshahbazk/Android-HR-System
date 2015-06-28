package momenify.proconnect.objects;

import java.util.Timer;

/**
 * Created by shahbazkhan on 5/25/15.
 */
public class KeepTimeObject {
    private String timeInGen;
    private String timeInDff;
    private String timeInEscaltions;
    private String timeInTraining;
    private String timeInPLayAuto;
    private Timer genTiner;
    private Timer dffTiner;
    private Timer escTiner;
    private Timer autoTiner;



    public void startTime(String value)
    {
        if(value == "In Gen queue")
        {

        }
        if(value == "In DFF queue")
        {

        }
        if(value == "In Play Auto queue")
        {

        }
        if(value == "In Esclations queue")
        {

        }



    }

    public void endTime(String value)
    {

        if(value == "In Gen queue")
        {

        }
        if(value == "In DFF queue")
        {

        }
        if(value == "In Play Auto queue")
        {

        }
        if(value == "In Esclations queue")
        {

        }

    }

    public void stopAllOtherExcept(String value)
    {

    }




}
