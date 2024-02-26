"use strict";

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.createUser = functions.auth.user().onCreate(async (user) => {
  await admin.firestore().collection("users").doc(user.uid).set({
    _uid: user.uid,
    email: user.email,
    phoneNumber: user.phoneNumber,
    picture: user.photoURL,
  });
  return;
});

/**
 * When a user deletes their account, clean up after them.
 */
exports.cleanupUser = functions.auth.user().onDelete(async (user) => {
  const dbRef = admin.firestore().collection("users");
  // const customer = (await dbRef.doc(user.uid).get()).data();
  // await stripe.customers.del(customer.customer_id);
  // Delete the customers payments & payment methods in firestore.
  // const snapshot = await dbRef
  //   .doc(user.uid)
  //   .collection('payment_methods')
  //   .get();
  // snapshot.forEach((snap) => snap.ref.delete());
  await dbRef.doc(user.uid).delete();
  return;
});
