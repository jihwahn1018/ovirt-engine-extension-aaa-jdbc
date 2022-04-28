package org.ovirt.engine.extension.aaa.jdbc.core;


public class KeyboardConsecution {
    private enum errorStatus {
        noErr, sameKey, consecutiveKey
    }
    private errorStatus errorFlag = errorStatus.noErr;

    public KeyboardConsecution() {}
    
    public boolean check(String target) {
        boolean ret = true;
        //check continuity of word in password
        if (!sameKeyCheck(target)) {
            errorFlag = errorStatus.sameKey;
            ret = false;
        }

        //check continuity of keyboard in password
        if(!consecutiveKeyCheck(target)) {
            errorFlag = errorStatus.consecutiveKey;
            ret = false;
        }
        return ret;
    }
    //Error message
    public String getUsage() {
        String ret = "";
        if (errorFlag == errorStatus.sameKey) {
            ret +=  String.format("%s \n", "Cannot use same character consecutively");
        }
        else if (errorFlag == errorStatus.consecutiveKey) {
            ret +=  String.format("%s \n", "Cannot use 4 consecutive letters on the keyboard");
        }
        return ret;
    }
    
    //check continuity of word in password
    public boolean sameKeyCheck(String target) {
        boolean ret = true;
        char pre_word = ' ';
        for (char word: target.toCharArray()) {
            if(pre_word == ' ') {
                pre_word = word;
                continue;
            }
            if(pre_word == word) {
                ret = false;
                break;
            }
            pre_word = word;
        }

        return ret;
    }
    //check continuity of keyboard in password
    public boolean consecutiveKeyCheck(String target) {
        boolean ret = true;
        String[] key_list = {"1234567890", "qwertyuiop", "asdfghjkl", "zxcvbnm", "QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM"};
        
        for(int i=0;i<target.length()-3;i++) {
            String subTarget = target.substring(i, i + 4);
            for(String key : key_list) {
                StringBuffer sb = new StringBuffer(key);
                String keyReverse = sb.reverse().toString();
                if(key.contains(subTarget) || keyReverse.contains(subTarget)) {
                    ret = false;
                }
            }
        }
    	return ret;
    }
}

