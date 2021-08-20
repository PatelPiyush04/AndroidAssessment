package com.theherdonline.db;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theherdonline.db.entity.DBUserType;
import com.theherdonline.db.entity.Role;
import com.theherdonline.db.entity.User;
import com.theherdonline.ui.general.ADType;
import com.theherdonline.util.UserType;

public class DataConverter {

    public static String USER_TYPE_AGENT = "agent";
    public static String USER_TYPE_PRODUCER = "producer";
    public static String USER_TYPE_USER = "user";


    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static List<User> str2Users(String string)
    {
        Gson mGson = new Gson();
        try {
            return Arrays.asList(mGson.fromJson(string, User[].class));
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @TypeConverter
    public static String users2str(List<User> userList)
    {
        Gson mGson = new Gson();
        return  mGson.toJson(userList);
    }



    @TypeConverter
    public static UserType Str2UserType(String str)
    {
        if (USER_TYPE_AGENT.equals(str))
        {
            return UserType.AGENT;
        }
        else if (USER_TYPE_PRODUCER.equals(str))
        {
            return UserType.PRODUCER;
        }
        else
        {
            return UserType.USER;
        }
    }



    @TypeConverter
    public static String UserType2Str(UserType userType)
    {
        if (userType == UserType.AGENT)
        {
            return USER_TYPE_AGENT;
        }
        else if (userType == UserType.PRODUCER)
        {
            return USER_TYPE_PRODUCER;
        }
        else
        {
            return USER_TYPE_USER;
        }
    }

    @TypeConverter
    public static DBUserType Str2DBUserType(String str)
    {
        return new DBUserType(Str2UserType(str));

    }

    @TypeConverter
    public static String DBUserType2Str(DBUserType userType)
    {
        return UserType2Str(userType.userType);
    }


    @TypeConverter
    public static List<Role> Role2ListRole(Role role)
    {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    @TypeConverter
    public static Role ListRole2Role(List<Role> roleList)
    {
        // if this user has an agent role, then he is agent.
        // or the last role is his role
        if (roleList == null)
        {
            return null;
        }
        Role curRole = null;
        for (Role r : roleList)
        {
            curRole = r;
            if (USER_TYPE_AGENT.equals(curRole.getName()))
            {
                return curRole;
            }
        }
        return curRole;
    }

    @TypeConverter
    public static UserType Roles2UserType(List<Role> roleList)
    {
        // if this user has an agent role, then he is agent.
        // or the last role is his role
        if (roleList == null || roleList.size() == 0)
        {
            return UserType.USER;
        }
        for (Role r : roleList)
        {
            if (USER_TYPE_AGENT.equals(r.getName()))
            {
                return UserType.AGENT;
            }
        }

        for (Role r : roleList)
        {
            if (USER_TYPE_PRODUCER.equals(r.getName()))
            {
                return UserType.PRODUCER;
            }
        }
        for (Role r : roleList)
        {
            if (USER_TYPE_USER.equals(r.getName()))
            {
                return UserType.USER;
            }
        }
        return UserType.USER;
    }

    @TypeConverter
    public static UserType Role2UserType(Role role)
    {
//        if
        return Str2UserType(role.getName());
    }

    @TypeConverter
    public static Role UserType2Role(UserType userType)
    {
        Role role = new Role();
        role.setName(UserType2Str(userType));
        return role;
    }

    @TypeConverter
    public static Integer ADType2Int(ADType adType)
    {
        //id    name    created_at    updated_at
        //1    Request    2019-01-08 00:25:59    2019-01-08 00:25:59
        //2    Draft    2019-01-08 00:25:59    2019-01-08 00:25:59
        //3    Scheduled    2019-01-08 00:25:59    2019-01-08 00:25:59
        //4    Published    2019-01-08 00:25:59    2019-01-08 00:25:59
        //5    Completed    2019-01-08 00:25:59    2019-01-08 00:25:59
        //6    Rejected    2019-01-10 00:49:12    2019-01-10 00:49:12
        if (adType == ADType.REQUEST)
        {
            return 1;
        }
        else if (adType == ADType.DRAFT)
        {
            return 2;
        }
        else if (adType == ADType.SCHEDULED)
        {
            return 3;
        }
        else if (adType == ADType.PUBLISHED)
        {
            return 4;
        }
        else if (adType == ADType.COMPLETED)
        {
            return 5;
        }
        else if (adType == ADType.REJECTED)
        {
            return 6;
        }
        else
        {
            return 7;
        }
    }

    @TypeConverter
    public static ADType Int2ADType(Integer id)
    {
        switch (id)
        {
            case 1:
                return ADType.REQUEST;
            case 2:
                return ADType.DRAFT;
            case 3:
                return ADType.SCHEDULED;
            case 4:
                return ADType.PUBLISHED;
            case 5:
                return ADType.COMPLETED;
            case 6:
                return ADType.REJECTED;
            default:
                return ADType.DRAFT;
        }
    }


    public static Integer Boolean2Inter(Boolean b)
    {
        return b ? 1 : 0;
    }


    public static Boolean Inter2Boolean(Integer b)
    {
        return b == null || b == 0 ? false : true;
    }

}
