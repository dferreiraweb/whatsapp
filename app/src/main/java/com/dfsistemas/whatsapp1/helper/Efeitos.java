package com.dfsistemas.whatsapp1.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

public class Efeitos {

    //Recebe tres parametros, O primeiro é a arrayList de todos os views que deseja o efeito
    //O segundo é a view que esta chamando o metodo.
    //O terceiro é o estilo (background) que deseja verificar se outro view que não seja o que chamou o metodo tenha
    public static void efeitoBorderBottom(Context context, ViewGroup viewPai, View viewChamando, Integer layoutPadrao, Integer layoutFoco) {
        Drawable efeitoPadrao = ContextCompat.getDrawable(context.getApplicationContext(), layoutPadrao);
        Drawable efeitoFoco = ContextCompat.getDrawable(context.getApplicationContext(), layoutFoco);


        if(viewChamando.getTag() != null){
            if(viewChamando.getTag().equals(layoutPadrao)) {
                Log.i("EFEITO", "TAG PADRAO DEFINIDA NESTE BOTAO: " + viewChamando.getTag());
                viewChamando.setBackground(efeitoFoco);
                viewChamando.setTag(layoutFoco);
                efeitoBorderBottom(context, viewPai, viewChamando, layoutPadrao, layoutFoco);
            }
            if(viewChamando.getTag().equals(layoutFoco)) {
                Log.i("EFEITO", "REPETINDO PQ O BOTAO FOI ATIVADO");
                for(int i = 0; i < viewPai.getChildCount(); i++) {
                    if(viewPai.getChildAt(i).getId() != viewChamando.getId()) {
                        viewPai.getChildAt(i).setBackground(efeitoPadrao);
                        viewPai.getChildAt(i).setTag(layoutPadrao);
                    }
                }
            }

        }else {
            //Cai na primeira repetição deixando todos os views como padrão
            //Adiciona numa array os views q tem background
            for(int i = 0; i < viewPai.getChildCount(); i++) {
                if(viewPai.getChildAt(i).getBackground() != null) {

                }
            }


//            efeitoBorderBottom(context, viewPai, viewChamando, layoutPadrao, layoutFoco);
        }
    }

    public static void fecharTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
