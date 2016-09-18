package hbie.vip.parking.db;


public class DBManager {

    private static DBManager instance = null;

    // 单例模式
    public static DBManager getInstance() {
        if (null == instance) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }
}