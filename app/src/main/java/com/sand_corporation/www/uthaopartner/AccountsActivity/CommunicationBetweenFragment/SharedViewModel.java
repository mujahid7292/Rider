package com.sand_corporation.www.uthaopartner.AccountsActivity.CommunicationBetweenFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by HP on 3/2/2018.
 */

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<SelectedDocument> selected = new MutableLiveData<SelectedDocument>();

    public void select(SelectedDocument selectedDocument) {
        selected.setValue(selectedDocument);
    }

    public LiveData<SelectedDocument> getSelected() {
        return selected;
    }
}
