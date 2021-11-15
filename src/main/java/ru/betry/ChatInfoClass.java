package ru.betry;

import org.bson.Document;

public class ChatInfoClass implements IChatInfo {
    //При добавлении важного поля, присутсвующего в базе данных
    //необходимо обновить метод MakeDocument()

    private static String studyGroup;
    private static String chatId;
    private static String userMessage;
    private static BotType botType;
    private static String userName;
    private static String state;

    public ChatInfoClass(String chatId, String userMessage, BotType botType) {
        ChatInfoClass.chatId = chatId;
        ChatInfoClass.userMessage = userMessage;
        ChatInfoClass.botType = botType;
        ChatInfoClass.userName = "None";
        ChatInfoClass.state = "new";
        ChatInfoClass.studyGroup = "None";
    }

    public ChatInfoClass(String chatId, String userMessage, BotType botType, String name) {
        ChatInfoClass.chatId = chatId;
        ChatInfoClass.userMessage = userMessage;
        ChatInfoClass.botType = botType;
        ChatInfoClass.userName = name;
        ChatInfoClass.state = "new";
        ChatInfoClass.studyGroup = "None";
    }

    public String getStudyGroup(){
        return studyGroup;
    }
    public void setStudyGroup(String studyGroup) { ChatInfoClass.studyGroup = studyGroup;}

    public String getChatId(){
        return chatId;
    }

    public String getUserMessage(){
        return userMessage;
    }

    public BotType getBotType(){
        return botType;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName) { ChatInfoClass.userName = userName;}

    public String getState() {
        return state;
    }
    public void setState(String state) {ChatInfoClass.state = state;}



    public void restartChat(){
        setStudyGroup("None");
        setState("new");
    }

    public void update(Document doc){
        if (doc == null) {
            setStudyGroup("None");
            setState("New");
        } else {
            setStudyGroup(doc.get("StudyGroup").toString());
            setState(doc.get("State").toString());
        }
    }


    Document makeDocument() {
        Document doc = new Document()
                .append("_id", chatId)
                .append("State", state)
                .append("StudyGroup", studyGroup)
                .append("Name", userName);
        return doc;
    }
}
