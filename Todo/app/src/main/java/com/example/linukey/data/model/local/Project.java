package com.example.linukey.data.model.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.linukey.data.model.remote.WebProject;
import com.example.linukey.data.source.local.DBHelper;
import com.example.linukey.data.source.local.ProjectContentProvider;
import com.example.linukey.data.source.local.SelfTaskContentProvider;
import com.example.linukey.util.TodoHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linukey on 12/3/16.
 */

public class Project extends TaskClassify implements Serializable {
    private String state;
    private String type;
    private final String TAG = this.getClass().getName();

    public Project(int id, String title, String content, String state, String projectId,
                   String userId, String type, String isdelete) {
        this.state = state;
        setId(id);
        setTitle(title);
        setContent(content);
        setUserId(userId);
        setSelfId(projectId);
        this.type = type;
        setIsdelete(isdelete);
    }

    public Project(String title, String content, String state, String projectId,
                   String userId, String type, String isdelete) {
        this.state = state;
        this.type = type;
        setTitle(title);
        setContent(content);
        setUserId(userId);
        setSelfId(projectId);
        setIsdelete(isdelete);
    }

    public Project(WebProject webProject){
        this.setId(webProject.getId());
        this.setTitle(webProject.getTitle());
        this.setContent(webProject.getContent());
        this.setState(webProject.getState());
        this.setType(webProject.getType());
        this.setUserId(webProject.getUserId());
        this.setSelfId(webProject.getSelfId());
        this.setIsdelete(webProject.getIsdelete());
    }

    public Project(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static int saveProject(Project project, Context context){

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectContentProvider.key_title, project.getTitle());
        contentValues.put(ProjectContentProvider.key_content, project.getContent());
        contentValues.put(ProjectContentProvider.key_state, project.getState());
        contentValues.put(ProjectContentProvider.key_projectId, project.getSelfId());
        contentValues.put(ProjectContentProvider.key_userId, project.getUserId());
        contentValues.put(ProjectContentProvider.key_type, project.getType());
        contentValues.put(ProjectContentProvider.key_isdelete, project.getIsdelete());

        Log.i("Project", "正在本地保存Project，值为:" + project.toString());

        ContentResolver cr = context.getContentResolver();
        Uri myRowUri = cr.insert(DBHelper.ContentUri_project, contentValues);

        if(myRowUri != null)
            return Integer.parseInt(myRowUri.getPathSegments().get(0));
        return -1;
    }

    public static boolean updateProject(Project project, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectContentProvider.key_title, project.getTitle());
        contentValues.put(ProjectContentProvider.key_content, project.getContent());
        contentValues.put(ProjectContentProvider.key_state, project.getState());
        contentValues.put(ProjectContentProvider.key_projectId, project.getSelfId());
        contentValues.put(ProjectContentProvider.key_userId, project.getUserId());
        contentValues.put(ProjectContentProvider.key_isdelete, project.getIsdelete());
        //无需更改type
        String where = ProjectContentProvider.key_id + " = " + project.getId();
        String[] selectionArgs = null;

        ContentResolver cr = context.getContentResolver();
        int id = cr.update(DBHelper.ContentUri_project, contentValues, where, selectionArgs);

        if(id>0)
            return true;

        return false;
    }

    /**
     * 删除所有项目
     * @return
     */
    public static boolean deleteAll(Context context){
        ContentResolver cr = context.getContentResolver();
        String where = null;
        String[] selectionArgs = null;
        int rid = cr.delete(DBHelper.ContentUri_project, where, selectionArgs);
        if(rid > -1)
            return  true;
        return false;
    }

    public static List<Project> getProjects(String projectType, Context context){
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = ProjectContentProvider.keys;

        String where = ProjectContentProvider.key_type + " = '" + projectType + "'";

        String selectionArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_project,
                result_columns, where, selectionArgs, order);

        List<Project> result = null;
        if(resultCursor != null && resultCursor.getCount() > 0){
            result = new ArrayList<>();
            while(resultCursor.moveToNext()){
                Project project = new Project(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7)
                );
                result.add(project);
            }
        }

        return result;
    }

    /**
     * 通过projectId获取projectName
     * @param projectId
     * @param context
     * @return
     */
    public static Project getProjectByProjectId(String projectId, Context context){
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = ProjectContentProvider.keys;

        String where = ProjectContentProvider.key_projectId + " = '" + projectId + "'";

        String selectionArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_project,
                result_columns, where, selectionArgs, order);

        Project project = null;
        if(resultCursor != null && resultCursor.getCount() > 0){
            while(resultCursor.moveToNext()){
                project = new Project(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7)
                );
            }
        }

        return project;
    }

    /**
     * 通过id查找project
     * @param id
     * @param context
     * @return
     */
    public static Project getProjectById(int id, Context context){
        ContentResolver cr = context.getContentResolver();

        String[] result_columns = ProjectContentProvider.keys;

        String where = ProjectContentProvider.key_id + " = " + id;

        String selectionArgs[] = null;
        String order = null;

        Cursor resultCursor = cr.query(DBHelper.ContentUri_project,
                result_columns, where, selectionArgs, order);

        Project project = null;
        if(resultCursor != null && resultCursor.getCount() > 0){
            while(resultCursor.moveToNext()){
                 project = new Project(
                        resultCursor.getInt(0),
                        resultCursor.getString(1),
                        resultCursor.getString(2),
                        resultCursor.getString(3),
                        resultCursor.getString(4),
                        resultCursor.getString(5),
                        resultCursor.getString(6),
                        resultCursor.getString(7)
                );
            }
        }

        return project;
    }

    @Override
    public String toString() {
        return super.toString() + "Project{" +
                "type='" + type + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
