package com.example.culturearound;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.core.CurrentActivity;
import com.example.culturearound.PretrazivanjeZnamenitosti.recyclerview.ZnamenitostRecyclerAdapter;
import com.example.database.EntitiesFirebase.Korisnik;
import com.example.database.EntitiesFirebase.Znamenitost;
import com.example.database.Listeners.UserListener;
import com.example.database.Listeners.ZnamenitostListener;
import com.example.database.UserHelper;
import com.example.database.ZnamenitostiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends Fragment implements ZnamenitostListener, UserListener {

    @BindView(R.id.favorites_search)
    SearchView searchView;

    @BindView(R.id.favorites_recycler)
    RecyclerView recyclerView;

    private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
    private ZnamenitostiHelper znamenitostiHelper;
    private UserHelper userHelper;

    private Korisnik currentUser;
    private String userId ;

    private List<Znamenitost> listOfFavorites;
    private List<Integer> allIdsOfFavorites;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        listOfFavorites = new ArrayList<>();

        userHelper = new UserHelper(getContext(),this);
        userId = userHelper.returnUserId();
        userHelper.findUserById(userId);


        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(CurrentActivity.getActivity(), listOfFavorites);
        recyclerView.setAdapter(favoritesRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        znamenitostiHelper = new ZnamenitostiHelper(CurrentActivity.getActivity(), this);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.clearFocus();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        List<Znamenitost> searchResult = searchFavoritesByName();
                        loadData(searchResult);
                        return false;
                    }
                }
        );
        searchView.setOnCloseListener(
                () -> {
                    searchView.clearFocus();
                    return false;
                }
        );

    }

    private void retrieveListOfFavorites( Korisnik currentUser) {
        allIdsOfFavorites = new ArrayList<>();
        for(Znamenitost temp : currentUser.getListaSpremljenihZnamenitosti()) {
            allIdsOfFavorites.add(temp.getIdZnamenitosti());
        }
        znamenitostiHelper.dohvatiSpremljeneZnamenitosti(allIdsOfFavorites);

    }

    private List <Znamenitost> searchFavoritesByName(){
        List<Znamenitost> searchResult = new ArrayList<Znamenitost>();
        for (Znamenitost temp: listOfFavorites){
            String favoriteName = temp.getNaziv();
            if (favoriteName.toLowerCase().contains(searchView.getQuery().toString().toLowerCase()))
                searchResult.add(temp);
        }
        return searchResult;
    }

    private void loadData (List<Znamenitost> listOfFavorites) {
        favoritesRecyclerAdapter.setZnamenitosti(listOfFavorites);
        favoritesRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoadZnamenitostSucess(String message, List<Znamenitost> listaZnamenitosti) {
        if (!listaZnamenitosti.isEmpty()){
            listOfFavorites = listaZnamenitosti;
            loadData(listOfFavorites);
        }
    }

    @Override
    public void onLoadZnamenitostFail(String message) {

    }

    @Override
    public void onLoadUserSuccess(String message, Korisnik user) {
        currentUser = user;
        retrieveListOfFavorites(currentUser);

    }

    @Override
    public void onLoadUserFail(String message) {

    }
}
