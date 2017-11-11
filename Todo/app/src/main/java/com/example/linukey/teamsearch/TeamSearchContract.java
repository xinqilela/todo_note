package com.example.linukey.teamsearch;

import com.example.linukey.data.model.local.TeamJoinInfo;
import com.example.linukey.data.model.remote.WebTeam;

import java.util.List;

/**
 * Created by linukey on 17-2-27.
 */

public class TeamSearchContract {
    interface TeamSearchView{

        void setSearchResult(List<WebTeam> dataSource);

        void showJoinTeamDialog(WebTeam team);
    }
    interface TeamSearchPresenter{

        void getTeamsByTeamName(String teamName);

        void saveJoinTeamInfoToWeb(TeamJoinInfo teamJoinInfo);

        WebTeam getTeamByPosition(int position);
    }
}
