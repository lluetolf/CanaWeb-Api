# Set Project ID
1. gcloud projects list
2. 

# Deploy
gcloud app deploy --project=dev-canaweb-firestore 

# token
curl --location --request POST 'https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyDxNB0QvG8UKQDnc1s7ik7oqsg-0CdW758' --header 'content-type: application/json' --data-raw '{"email": "lukas.luetolf@gmail.com", "password": "abc123", "returnSecureToken": true }' |grep idToken >
token.txt