package server.entities;

public class Quiz {
    int quiz_Id, users_Id_f;
    String name;

    public Quiz(int quiz_Id, int users_Id_f, String name) {
        this.quiz_Id = quiz_Id;
        this.users_Id_f = users_Id_f;
        this.name = name;
    }
    
    public Quiz(int quiz_Id, String name) {
        this.quiz_Id = quiz_Id;
        this.name = name;
    }

    public int getQuiz_Id() {
        return quiz_Id;
    }

    public int getUsers_Id_f() {
        return users_Id_f;
    }

    public String getName() {
        return name;
    }

    public void setQuiz_Id(int quiz_Id) {
        this.quiz_Id = quiz_Id;
    }

    public void setUsers_Id_f(int users_Id_f) {
        this.users_Id_f = users_Id_f;
    }

    public void setName(String name) {
        this.name = name;
    }
}
