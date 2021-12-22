package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

public interface HomeActionInterface {

    /**
     *
     *  use this to stop the beaconService Running in the background
     *
     */
    void changeBeaconServiceState(Integer id, Boolean enable);

}
