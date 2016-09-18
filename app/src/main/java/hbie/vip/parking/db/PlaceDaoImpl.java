package hbie.vip.parking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hbie.vip.parking.activity.wheel.CityModel;
import hbie.vip.parking.activity.wheel.DistrictModel;
import hbie.vip.parking.activity.wheel.ProvinceModel;
import hbie.vip.parking.bean.PlaceInfo;

public class PlaceDaoImpl implements PlaceDaoInface {
    public Database db_Helper = null;

    public PlaceDaoImpl(Context context) {
        db_Helper = new Database(context);
    }

    /**
     * 批量插入
     */
    @Override
    public boolean addPlaceList(ArrayList<ContentValues> list) {
        boolean ret = false;
        SQLiteDatabase database = null;
        try {
            database = db_Helper.getWritableDatabase();
            database.beginTransactionNonExclusive();
            clearFeedTable();
            for (ContentValues v : list) {
                database.insert(Const_DB.DATABASE_TABLE_PLACE, null, v);
            }
            database.setTransactionSuccessful();
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
        return ret;
    }

    @Override
    public ArrayList<PlaceInfo> getProvinceList() {
        ArrayList<PlaceInfo> places = new ArrayList<PlaceInfo>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, null, null, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID, null, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    PlaceInfo place = new PlaceInfo();
                    place = getPlaceByCursor(cursor);
                    places.add(place);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return places;
    }

    @Override
    public ArrayList<PlaceInfo> getCityListByProvinceId(String provinceId) {
        ArrayList<PlaceInfo> places = new ArrayList<PlaceInfo>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID + " is '" + provinceId + "'", null,
                    Const_DB.DATABASE_TABLE_PLACE_CITY_ID, null, Const_DB.DATABASE_TABLE_PLACE_CITY_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    PlaceInfo place = new PlaceInfo();
                    place = getPlaceByCursor(cursor);
                    places.add(place);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return places;
    }

    @Override
    public ArrayList<PlaceInfo> getDistrictListByCityId(String cityId) {
        ArrayList<PlaceInfo> places = new ArrayList<PlaceInfo>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_CITY_ID + " is '" + cityId + "'", null,
                    Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID, null, Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    PlaceInfo place = new PlaceInfo();
                    place = getPlaceByCursor(cursor);
                    places.add(place);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return places;
    }

    @Override
    public PlaceInfo getPlaceByDistrictId(String districtId) {
        PlaceInfo place = new PlaceInfo();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID + " is '" + districtId + "'", null, null, null, null);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    place = getPlaceByCursor(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return place;
    }

    @Override
    public ProvinceModel getPlaceByProvinceName(String provinceName) {
        ProvinceModel province = new ProvinceModel();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_NAME + " like '%" + provinceName + "%'", null,
                    Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID, null, null);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    province = getProvinceModelByCursor(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return province;
    }

    @Override
    public CityModel getPlaceByCityName(String cityName) {
        CityModel city = new CityModel();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_CITY_NAME + " like '%" + cityName + "%'", null,
                    Const_DB.DATABASE_TABLE_PLACE_CITY_ID, null, null);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    city = getCityModelByCursor(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return city;
    }

    @Override
    public DistrictModel getPlaceByDistrictName(String districtName) {
        DistrictModel district = new DistrictModel();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_DISTRICT_NAME + " like '%" + districtName + "%'", null,
                    Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID, null, null);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    district = getDistrictModelByCursor(cursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return district;
    }

    private PlaceInfo getPlaceByCursor(Cursor cursor) {
        PlaceInfo place = new PlaceInfo();
        place.setProvinceId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID)));
        place.setProvinceName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_NAME)));
        place.setCityId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_CITY_ID)));
        place.setCityName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_CITY_NAME)));
        place.setDistrictId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID)));
        place.setDistrictName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_NAME)));
        return place;
    }

    @Override
    public ArrayList<ProvinceModel> getProvinceModelList() {
        ArrayList<ProvinceModel> provinces = new ArrayList<ProvinceModel>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, null, null, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID, null, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    ProvinceModel provinceModel = new ProvinceModel();
                    provinceModel = getProvinceModelByCursor(cursor);
                    ArrayList<CityModel> citys = getCityModelListByProvinceId(provinceModel.getId());
                    provinceModel.setCityList(citys);
                    provinces.add(provinceModel);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return provinces;
    }

    private ProvinceModel getProvinceModelByCursor(Cursor cursor) {
        ProvinceModel provinceModel = new ProvinceModel();
        provinceModel.setId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID)));
        provinceModel.setName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_NAME)));
        return provinceModel;
    }

    @Override
    public ArrayList<CityModel> getCityModelListByProvinceId(String provinceId) {
        ArrayList<CityModel> citys = new ArrayList<CityModel>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID + " is '" + provinceId + "'", null,
                    Const_DB.DATABASE_TABLE_PLACE_CITY_ID, null, Const_DB.DATABASE_TABLE_PLACE_CITY_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    CityModel cityModel = new CityModel();
                    cityModel = getCityModelByCursor(cursor);
                    ArrayList<DistrictModel> districts = getDistrictModelListByCityId(cityModel.getId());
                    cityModel.setDistrictList(districts);
                    citys.add(cityModel);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return citys;
    }

    private CityModel getCityModelByCursor(Cursor cursor) {
        CityModel cityModel = new CityModel();
        cityModel.setId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_CITY_ID)));
        cityModel.setName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_CITY_NAME)));
        return cityModel;
    }

    @Override
    public ArrayList<DistrictModel> getDistrictModelListByCityId(String cityId) {
        ArrayList<DistrictModel> districts = new ArrayList<DistrictModel>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = db_Helper.getReadableDatabase();
            cursor = database.query(Const_DB.DATABASE_TABLE_PLACE, Const_DB.PLACE_PROJECTION, Const_DB.DATABASE_TABLE_PLACE_CITY_ID + " is '" + cityId + "'", null,
                    Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID, null, Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID);
            if (cursor.getColumnCount() > 0) {
                while (cursor.moveToNext()) {
                    DistrictModel districtModel = new DistrictModel();
                    districtModel = getDistrictModelByCursor(cursor);
                    districts.add(districtModel);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return districts;
    }

    private DistrictModel getDistrictModelByCursor(Cursor cursor) {
        DistrictModel districtModel = new DistrictModel();
        districtModel.setId(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID)));
        districtModel.setName(cursor.getString(cursor.getColumnIndex(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_NAME)));
        return districtModel;
    }

    public void clearFeedTable() {
        String sql = "DELETE FROM " + Const_DB.DATABASE_TABLE_PLACE + ";";
        SQLiteDatabase db = db_Helper.getWritableDatabase();
        db.execSQL(sql);
        revertSeq();
    }

    private void revertSeq() {
        String sql = "update sqlite_sequence set seq=0 where name='" + Const_DB.DATABASE_TABLE_PLACE + "'";
        SQLiteDatabase db = db_Helper.getWritableDatabase();
        db.execSQL(sql);
    }
}
