package io.gitlab.scriptkid.ezcheatclient.misc;

/* Sends some messages to the player if he/she kills the ender dragon */
public class ThankYou implements Runnable {

    public Boolean runOnce = false;

    @Override
    public void run() {

        try {

            if(runOnce == false) {

                runOnce = true;

                utils.sayAsScriptKid("I can't believe you did it!");

                Thread.sleep(4000);

                utils.sayAsScriptKid("You really have no life do you?");

                Thread.sleep(4000);

                utils.sayAsScriptKid("Tag me on twitter and let me know you beat me!");

                Thread.sleep(3000);

                utils.sayAsScriptKid("@ScriptKidHacks");

            }

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
