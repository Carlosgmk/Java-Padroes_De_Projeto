import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

// Gerenciador de tarefas
class TaskManager {
    private static TaskManager instance;
    private List<Task> tasks;

    private TaskManager() {
        tasks = new ArrayList<>();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void completeTask(Task task) {
        tasks.remove(task);
        System.out.println("Tarefa concluída: " + task.getDescription());
    }

    public void listTasks() {
        System.out.println("Tarefas pendentes:");
        for (Task task : tasks) {
            System.out.println("- " + task.getDescription());
        }
    }
}

// Interface para criar tarefas
interface TaskFactory {
    Task createTask(String description);
}

// Criação de tarefas
class WorkTaskFactory implements TaskFactory {
    @Override
    public Task createTask(String description) {
        return new WorkTask(description);
    }
}

class PersonalTaskFactory implements TaskFactory {
    @Override
    public Task createTask(String description) {
        return new PersonalTask(description);
    }
}

//  Observador de tarefas concluídas
class TaskObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Task) {
            System.out.println("Tarefa concluída: " + ((Task) arg).getDescription());
        }
    }
}


class Task extends Observable {
    private String description;

    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void markComplete() {
        setChanged();
        notifyObservers(this);
    }
}

// Tarefa de trabalho
class WorkTask extends Task {
    public WorkTask(String description) {
        super(description);
    }
}

// Tarefa pessoal
class PersonalTask extends Task {
    public PersonalTask(String description) {
        super(description);
    }
}

public class TaskManagementApp {
    public static void main(String[] args) {
        // Criando o gerenciador de tarefas (Singleton)
        TaskManager taskManager = TaskManager.getInstance();

        // Criando observadores de tarefas concluídas
        TaskObserver observer1 = new TaskObserver();
        TaskObserver observer2 = new TaskObserver();
        taskManager.addObserver(observer1);
        taskManager.addObserver(observer2);

       
        TaskFactory workTaskFactory = new WorkTaskFactory();
        TaskFactory personalTaskFactory = new PersonalTaskFactory();

        Task workTask = workTaskFactory.createTask("Concluir relatório de vendas");
        Task personalTask = personalTaskFactory.createTask("Fazer compras no supermercado");

        
        taskManager.addTask(workTask);
        taskManager.addTask(personalTask);

       
        taskManager.listTasks();

        workTask.markComplete();

        // Listando tarefas pendentes novamente
        taskManager.listTasks();
    }
}