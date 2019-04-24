// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
const firestore = admin.firestore()

exports.payUser = functions.firestore
  .document('bets/{betId}')
  .onUpdate((change,context) => {
    const finalBet = change.after.data();
    if (finalBet.active === -1) {
      const usersRef = firestore.collection("users");
      const betAmount = finalBet.amount;
      const favoriteUser = finalBet.betOnFavorite;
      const underdogUser = finalBet.betOnUnderdog;
      const winningUser = finalBet.winner;
      console.log("winner was " + winningUser);
      var favActivePoints = 0;
      var favPoints = 0;
      var underPoints = 0;
      var underActivePoints = 0;
      var newPoints = 0;
      var newActivePoints = 0;

      var getDocFav = usersRef.doc(favoriteUser).get()
      .then(
        doc => {
          if (!doc.exists) {
            console.log('No such document!');
          }
          else {
            var userData = doc.data();
            favActivePoints = userData.activePoints;
            favPoints = userData.points;
            if (winningUser === favoriteUser) {
              console.log("FavoriteUser Won " + betAmount + " increasing his point total");
              newPoints = betAmount + favPoints;
              console.log("Removing points from activePoints as bet is over");
              newActivePoints = favActivePoints - betAmount;
              var updateFavUserWin = usersRef.doc(favoriteUser).update({points: newPoints, activePoints: newActivePoints});
            }
            else {
              console.log("Removing points from activePoints as bet is over, favoriteUser lost");
              newActivePoints = favActivePoints - betAmount;
              var updateFavUserLoss = usersRef.doc(favoriteUser).update({activePoints: newActivePoints});
            }
          }
          return 0;
        })
        .catch(err => {
          console.log('Error getting document', err);
      });
      var getDocUnder = usersRef.doc(underdogUser).get()
      .then(
        doc => {
          if (!doc.exists) {
            console.log('No such document!');
          } else {
            var userData = doc.data();
            underActivePoints = userData.activePoints;
            underPoints = userData.points;
            if (winningUser === underdogUser) {
              console.log("FavoriteUser Won " + betAmount + " increasing his point total");
              newPoints = betAmount + underPoints;
              console.log("Removing points from activePoints as bet is over");
              newActivePoints = underActivePoints - betAmount;
              var updateUnderdogUserFav = usersRef.doc(underdogUser).update({points: newPoints, activePoints: newActivePoints});
            }
            else {
              console.log("Removing points from activePoints as bet is over, underdogUser lost");
              newActivePoints = favActivePoints - betAmount;
              var updateUnderdogUserLoss = usersRef.doc(underdogUser).update({activePoints: newActivePoints});

            }
          }
          return 0;
        })
        .catch(err => {
          console.log('Error getting document', err);
        }
      );
    }

  });


exports.determineBetWinner = functions.firestore
    .document('games/{gameId}')
    .onUpdate((change, context) => {
      // Get an object representing the document
      // e.g. {'name': 'Marie', 'age': 66}
      const finalGame = change.after.data();
      const away_score = finalGame.score_away;
      const home_score = finalGame.score_home;
      const gameId = change.after.id;

      var betsRef = firestore.collection("bets");
      var query = betsRef.where("gameRef", "==", gameId).where("active", "==", 1);
      query.get().then(function(querySnapshot) {
        if (querySnapshot.empty) {
          throw new Error('no documents found');
        }
        querySnapshot.forEach(function (documentSnapshot) {
          var data = documentSnapshot.data();
          var winningUser = "push";
          console.log(data.betOnFavorite + " and " + data.betOnUnderdog + " bet on " + data.home + " vs. " + data.away);
          var bet_amount = data.amount
          console.log("bet type was " + data.type + " and the wager was for " + bet_amount.toString());
          if (data.type === "spread") {
            var favorite = "away";
            if (data.home === data.favorite) {
              favorite = "home";
            }
            if (favorite === "home") {
              if ((home_score - away_score) > data.odds) {
                console.log("Home Favorite Beat Odds");
                winningUser = data.betOnFavorite;
              }
              else if ((home_score - away_score) < data.odds) {
                console.log("Home Favorite Lost Odds");
                winningUser = data.betOnUnderdog;
              }
              else {
                console.log("push");
                winningUser = "house";
              }
            }
            else {
              if ((away_score - home_score) > data.odds) {
                console.log("Away Favorite Beat Odds");
                winningUser = data.betOnFavorite;
              }
              else if ((away_score - home_score) < data.odds) {
                console.log("Away Favorite Lost Odds");
                winningUser = data.betOnUnderdog;
              }
              else {
                console.log("push");
                winningUser = "house";
              }
            }
          }
          else {
            if ((home_score + away_score) > data.odds) {
              console.log("hit the over");
              winningUser = data.betOnFavorite;

            }
            else if ((home_score + away_score) < data.odds) {
              console.log("didnt meet line");
              winningUser = data.betOnUnderdog;
            }
            else {
              console.log("push");
              winningUser = "house";
            }
          }
           var updateWinner = betsRef.doc(documentSnapshot.id).update({winner: winningUser, active: -1});
          console.log(documentSnapshot.id);
         });
         return querySnapshot;
       }).catch(e => {
         console.log(e);
       });
     });
