package ru.betry;

public class ChatInfoClass implements IChatInfo {

    private static String dataBaseId;
    private static String studyGroup;
    private static String chatId;
    private static String userMessage;
    private static BotType botType;

    public ChatInfoClass(String chatId, String userMessage, BotType botType) {
        //ChatInfoClass.dataBaseId = dataBaseId;
        ChatInfoClass.chatId = chatId;
        ChatInfoClass.userMessage = userMessage;
        ChatInfoClass.botType = botType;
    }

    public String getDataBaseId(){
        return dataBaseId;
    }

    public String getStudyGroup(){
        return studyGroup;
    }

    public String getChatId(){
        return chatId;
    }

    public String getUserMessage(){
        return userMessage;
    }

    public BotType getBotType(){
        return botType;
    }

    public void restartChat(){
        studyGroup = "None";
        updateToDataBase();
    }


    public void updateFromDataBase(){
        //todo написть обновление из базы данных
    }

    public void updateToDataBase(){
        //todo написть обновление в базу данных
    }
}
