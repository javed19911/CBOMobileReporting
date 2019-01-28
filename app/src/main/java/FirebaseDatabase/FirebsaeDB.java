package FirebaseDatabase;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class FirebsaeDB<T> {

    private static final String TAG = "FirebaseDb";
    private String GMail_UserId="";
    private String GMail_Password="";
    private String BaseURL ="";
    private String table ="";


    private FirebaseDatabase mfirebaseDatabase;


    private DatabaseReference rootRef;;
    private boolean  primaryKeyAutoGenrate= true;

    private Response response = null;






    public boolean isPrimaryKeyAutoGenrate() {
        return primaryKeyAutoGenrate;
    }

    public void setPrimaryKeyAutoGenrate(boolean primaryKeyAutoGenrate) {
        this.primaryKeyAutoGenrate = primaryKeyAutoGenrate;

    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;

            login (new ILogin () {
                @Override
                public void onSuccess(DatabaseReference rootRef) {
                    rootRef.addValueEventListener (new ValueEventListener () {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (response !=null){
                                response.onTableUpdated (dataSnapshot);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (response !=null){
                                response.onTableUpdateError ("","");
                            }
                        }
                    });
                }

                @Override
                public void onError(String title, String description) {
                    if (response !=null) {
                        response.onLoginError (title, description);
                    }
                }

            });


    }

    public  interface Response<T>{

        void onLoginSuccess(DatabaseReference DBRef);
        void onLoginError(String title, String description);
        void onTableInserted(T model);
        void onTableInsertError(String table, T model, String description);
        void onTableDeleted(T model);
        void onTableDeletedError(T model, String description);
        void onTableUpdated(DataSnapshot dataSnapshot);
        void onTableUpdateError(String title, String description);
    }

    public  interface ILogin{
        void onSuccess(DatabaseReference DBRef);
        void onError(String title, String description);
    }

    public FirebsaeDB(String GMail_UserId, String GMail_Password) {
        this.GMail_UserId = GMail_UserId;
        this.GMail_Password = GMail_Password;
        setupFirebaseAuth();
    }

    public String getBaseURL() {
        return BaseURL;
    }

    public void setBaseURL(String baseURL) {
        BaseURL = baseURL;
    }


    public void setResponse(Response response) {
        this.response = response;

    }

    public Response getResponse() {
        return response;
    }

    private void setupFirebaseAuth() {
        //auth=FirebaseAuth.getInstance ();
      /* mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // UserAccountSettings is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // UserAccountSettings is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        auth.addAuthStateListener(mAuthListener);*/


    }

   /* @Override
    protected void finalize() throws Throwable {
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
        super.finalize ();
    }*/

    protected   void login( final ILogin response){
        if(getTable ().trim ().isEmpty ()){
            response.onError("Table Error ","Please  specfied table name");
            return;
        }

        //FirebaseUser user = auth.getCurrentUser ();
        //if (user != null){
            mfirebaseDatabase = FirebaseDatabase.getInstance ();
            rootRef = mfirebaseDatabase.getReference ();
            rootRef = FirebaseDatabase.getInstance ().getReference (BaseURL);
            rootRef= rootRef.child (getTable ());

            response.onSuccess (rootRef);
            return;
        //}

       /* if(GMail_UserId.trim ().isEmpty () &&  GMail_Password.trim ().isEmpty ()){
            if(response!=null) {
                response.onError("Login Failed ","Invalid Userid/Password");
            }

        } else if(GMail_UserId.matches (String.valueOf (!Patterns.EMAIL_ADDRESS.matcher(GMail_UserId).matches()))) {
            if(response!=null) {
                response.onError("Invalid Email","Please enter a valid email");
            }
        }else {
            mfirebaseDatabase = FirebaseDatabase.getInstance ();
            rootRef = mfirebaseDatabase.getReference ();
            rootRef = FirebaseDatabase.getInstance ().getReference (BaseURL);
            rootRef= rootRef.child (getTable ());
            if(response!=null){
                response.onSuccess (rootRef);
            }
            return;*/
            /*auth.signInWithEmailAndPassword(GMail_UserId, GMail_Password)
                    .addOnCompleteListener( context, new OnCompleteListener<AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser user = auth.getCurrentUser();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                if(response!=null){

                                    response.onError("Login Failed Error","Inavlid UserId/Password ");
                                }
                            }
                            else {

                                if(user.isEmailVerified()){
                                    mfirebaseDatabase = FirebaseDatabase.getInstance ();
                                    rootRef = mfirebaseDatabase.getReference ();
                                    rootRef = FirebaseDatabase.getInstance ().getReference (BaseURL);
                                    rootRef= rootRef.child (getTable ());
                                    if(response!=null){

                                        response.onSuccess (auth);
                                    }
                                }else{

                                    if(response!=null){
                                        response.onError("Verfication pending","Please verify your email");
                                    }
                                    auth.signOut();
                                }
                            }
                        }
                    });*/
        //}


    }


    public   void insert(final T model){
        insert(model,null);
    }


    public   void insert(final T model,String primerykey){
        try {


            login ( new ILogin () {

                @Override
                public void onSuccess(DatabaseReference rootRef) {


                    if (isPrimaryKeyAutoGenrate ()) {
                        rootRef.push ().setValue (model);
                    } else {
                        if (primerykey != null) {
                            rootRef = rootRef.child(primerykey);
                        }
                        rootRef.setValue (model);
                    }
                    if (response !=null){

                        response.onTableInserted (model);
                    }
                }

                @Override
                public void onError(String title, String description) {
                    if (response !=null) {
                        response.onLoginError (title, description);
                    }
                }

            });

        }catch (Exception e){
            e.printStackTrace ();
            if (response !=null){
                response.onTableInsertError (table,model,e.getMessage ());
            }
        }
    }


    public   void delete(final T model){
        delete(model,null);
    }

    public void delete( final T model,String primerykey){
        try {
            login ( new ILogin () {
                @Override
                public void onSuccess(DatabaseReference rootRef) {

                    if (primerykey != null) {
                        rootRef = rootRef.child(primerykey);
                    }
                    rootRef.removeValue ();
                    if (response !=null){
                        response.onTableDeleted (model);
                    }

                }

                @Override
                public void onError(String title, String description) {
                    if (response !=null) {
                        response.onTableDeletedError (title, description);
                    }
                }

            });

        }catch (Exception e){
            e.printStackTrace ();
            if (response !=null){
                response.onTableDeletedError (model,e.getMessage ());
            }
        }
    }
 /*   protected   void update(Activity context, final String table, final HashMap<String, Object>primerykeyupdate){
        try {
            login (context, new ILogin () {
                @Override
                public void onSuccess(FirebaseAuth auth) {



                    tableRef.child (table).updateChildren (primerykeyupdate);
                    if (response !=null){
                        response.onTableUpdated (primerykeyupdate);
                    }
                }

                @Override
                public void onError(String title, String description) {
                    response.onTableUpdateError (title,description );
                }

            });

        }catch (Exception e){
            e.printStackTrace ();
            if (response !=null){
                response.onTableUpdateError (table,e.getMessage ());
            }
        }
    }*/

}
