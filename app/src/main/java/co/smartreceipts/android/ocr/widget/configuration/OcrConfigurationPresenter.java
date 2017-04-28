package co.smartreceipts.android.ocr.widget.configuration;

import javax.inject.Inject;

import co.smartreceipts.android.di.scopes.FragmentScope;
import co.smartreceipts.android.utils.log.Logger;
import co.smartreceipts.android.widget.viper.BasePresenter;

@FragmentScope
public class OcrConfigurationPresenter extends BasePresenter<OcrConfigurationView, OcrConfigurationInteractor> {

    @Inject
    public OcrConfigurationPresenter(OcrConfigurationView view, OcrConfigurationInteractor interactor) {
        super(view, interactor);
    }

    @Override
    public void subscribe() {
        view.present(interactor.getEmail());

        // Set the current checkbox value
        compositeDisposable.add(interactor.getAllowUsToSaveImagesRemotely()
                .subscribe(view.getAllowUsToSaveImagesRemotelyConsumer()));

        // Persist values from checkbox toggling
        compositeDisposable.add(view.getAllowUsToSaveImagesRemotelyCheckboxChanged()
                .doOnNext(saveImagesRemotely -> Logger.debug(OcrConfigurationPresenter.this, "Updating saveImagesRemotely setting: {}", saveImagesRemotely))
                .subscribe(interactor::setAllowUsToSaveImagesRemotely));

        // Show remaining scans
        compositeDisposable.add(interactor.getRemainingScansStream()
                .subscribe(view::present));

        // Show available purchases list
        compositeDisposable.add(interactor.getAvailableOcrPurchases()
                .doOnSuccess(availablePurchases -> Logger.info(OcrConfigurationPresenter.this, "Presenting list of purchases: {}.", availablePurchases))
                .subscribe(view::present,
                    throwable -> Logger.warn(OcrConfigurationPresenter.this, "Failed to get available purchases.", throwable)));

        // Track user purchase clicks
        compositeDisposable.add(view.getAvailablePurchaseClicks()
                .doOnNext(availablePurchase -> Logger.info(OcrConfigurationPresenter.this, "User clicked to buy purchase: {}.", availablePurchase))
                .subscribe(interactor::startOcrPurchase));
    }

}
