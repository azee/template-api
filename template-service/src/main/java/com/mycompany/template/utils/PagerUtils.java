package com.mycompany.template.utils;

import com.mycompany.template.beans.Pager;
import com.mycompany.template.beans.PagerItem;
import com.mycompany.template.beans.Parameter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 7/23/13
 * Time: 8:17 PM
  */
@Service
public class PagerUtils {
    public Pager getPager(long count, int step, int startWith, List<Parameter> parameters) {
        int currPageNumber;
        int startingPageNumber;

        String xtraParams = "";
        for (Parameter parameter : parameters){
            xtraParams = xtraParams + "&" + parameter.getName() + "=" + parameter.getValue();
        }

        Pager pager = new Pager();
        pager.setPagesWindow(7);
        pager.setCentralPage(4);
        pager.setCount(count);
        pager.setStartWith(startWith);
        pager.setStep(step);

        //Detecting number or pages
        pager.setNumberOfPages((int)(count/step));
        if (count % step > 0) {
            pager.setNumberOfPages(pager.getNumberOfPages() + 1);
        }

        //Detecting if there are less pages than pageWindow size
        if (pager.getNumberOfPages() < pager.getPagesWindow()){
            pager.setPagesWindow(pager.getNumberOfPages());
        }


        //Adding "Prev" page
        PagerItem prevPage = new PagerItem();
        prevPage.setActive(false);
        prevPage.setTitle("← Previous");
        prevPage.setArgs("skip=" + (startWith - step) + "&limit=" + step + xtraParams);

        //Are we on the 1-st page?
        if (startWith > 0){
            prevPage.setEnabled(true);
        }
        else {
            prevPage.setEnabled(false);
        }
        pager.getPages().add(prevPage);


        //Loocking for the first page to draw
        currPageNumber = startWith / step + 1;
        if (currPageNumber <= pager.getCentralPage()){
            //We are in the 1-st window
            startingPageNumber = 1;
        }
        else{
            if (currPageNumber + (pager.getPagesWindow() - pager.getCentralPage()) >= pager.getNumberOfPages()){
                //We are in the last window - don't shift the pager
                startingPageNumber = pager.getNumberOfPages() - pager.getPagesWindow() + 1;
            }
            else    {
                //Shifting the pager
                startingPageNumber = currPageNumber - pager.getCentralPage() + 1;
            }
        }

        //Filing pages
        for (int i = startingPageNumber; (i < startingPageNumber + pager.getPagesWindow()) & (i <= pager.getNumberOfPages()); i++){
            PagerItem pagerItem = new PagerItem();
            pagerItem.setTitle(Integer.toString(i));
            pagerItem.setPageNumber(i);
            pagerItem.setArgs("skip=" + (i - 1) * step  + "&limit=" + step + xtraParams);
            if ((startWith / step + 1) == i){
                pagerItem.setActive(true);
                pagerItem.setEnabled(false);
            }
            else {
                pagerItem.setActive(false);
                pagerItem.setEnabled(true);
            }

            pager.getPages().add(pagerItem);

        }

        //Adding "Next" page
        PagerItem nextPage = new PagerItem();
        nextPage.setActive(false);
        nextPage.setTitle("Next →");
        nextPage.setArgs("skip=" + (startWith + step)  + "&limit=" + step + xtraParams);

        //Are we on the last page?
        if (startWith + step > count){
            nextPage.setEnabled(false);
        }
        else {
            nextPage.setEnabled(true);
        }
        pager.getPages().add(nextPage);


        //Change pages titles to "..." if in the middle of the pager.
        //Don't forget - first and last objects in list are "Next" and "Prev" buttons
        if (pager.getPages().size() > 1){
            if (pager.getPages().get(1).getPageNumber() > 1){
                pager.getPages().get(1).setTitle("...");
            }
            if (pager.getPages().get(pager.getPages().size() - 2).getPageNumber() < pager.getNumberOfPages()){
                pager.getPages().get(pager.getPages().size() - 2).setTitle("...");
            }
        }
        return pager;
    }
}
