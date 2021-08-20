package com.theherdonline.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.theherdonline.db.Dao.JsonDao;
import com.theherdonline.db.Dao.LivestockDao;
import com.theherdonline.db.Dao.AgentDao;
import com.theherdonline.db.Dao.AnimalDao;
import com.theherdonline.db.Dao.BreedDao;
import com.theherdonline.db.Dao.GenderEntityDao;
import com.theherdonline.db.Dao.LivestockCategoryDao;
import com.theherdonline.db.Dao.LivestockSubCategoryDao;
import com.theherdonline.db.Dao.MarketDataDao;
import com.theherdonline.db.Dao.MarketReportDao;
import com.theherdonline.db.Dao.NotificationDao;
import com.theherdonline.db.Dao.OrgDao;
import com.theherdonline.db.Dao.PaymentCardDao;
import com.theherdonline.db.Dao.PregnancyEntityDao;
import com.theherdonline.db.Dao.SaleyardCategoryDao;
import com.theherdonline.db.Dao.UserDao;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Agent;
import com.theherdonline.db.entity.AnimalCategory;
import com.theherdonline.db.entity.BreedCategory;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.JsonEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.MarketData;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.db.entity.User;

@Database(entities = {MarketData.class,
        Agent.class,
        User.class,
        EntityLivestock.class,
        BreedCategory.class,
        AnimalCategory.class,
        //LivestockStatus.class,
        MarketReport.class,
        PaymentCard.class,
        LivestockCategory.class,
        LivestockSubCategory.class,
        SaleyardCategory.class,
        GenderEntity.class,
        Organisation.class,
        PregnancyEntity.class,
        HerdNotification.class,
        JsonEntity.class}, version = 29, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarketDataDao marketDataDao();
    public abstract AgentDao agentDao();
    public abstract UserDao userDao();
    public abstract LivestockDao advertisementDao();
    public abstract BreedDao breedDao();
    public abstract AnimalDao animalDao();
    //public abstract LivestockStatusDao livestockStatusDao();
    public abstract PaymentCardDao paymentCardDao();
    public abstract LivestockCategoryDao livestockCategoryDao();
    public abstract LivestockSubCategoryDao livestockSubCategoryDao();
    public abstract SaleyardCategoryDao saleyardCategoryDao();
    public abstract MarketReportDao marketReportDao();
    public abstract GenderEntityDao genderEntityDao();
    public abstract PregnancyEntityDao pregnancyEntityDao();
    public abstract OrgDao orgDao();
    public abstract NotificationDao notificationDao();
    public abstract JsonDao jsonDao();


    static public int JSON_NOTIFICATION = 1;


    public void clearDBWhenShutdown(Integer userId)
    {
        marketReportDao().deleteAll();
        agentDao().deleteAll();
        if (userId != null)
            userDao().deleteOthers(userId);
        advertisementDao().deleteAll();
        //breedDao().deleteAll();
        //animalDao().deleteAll();
        paymentCardDao().deleteAll();
        //livestockCategoryDao().deleteAll();
        //livestockSubCategoryDao().deleteAll();
        //saleyardCategoryDao().deleteAll();
        marketReportDao().deleteAll();
        //genderEntityDao().deleteAll();
        //pregnancyEntityDao().deleteAll();
        orgDao().deleteAll();
        notificationDao().deleteAll();
        jsonDao().deleteAll();
    }





}
