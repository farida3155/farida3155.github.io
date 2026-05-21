#include <iostream>
#include <string>
#include <vector>
using namespace std;

enum genre {
    rock, pop, hiphop, jazz, blues, classic, country, electronic, musical_theatre
};

struct album {
    string album_name;
    string artist_name;
    genre genre;
    double price;
};

struct ordered_album {
    string name;
    int quantity;
    double price;
};

struct order {
    int order_ID;
    int customer_ID;
    double total;
    ordered_album items[10];
    int item_count = 0;
    string address;
};

const string GenreNames[] = { "Rock", "Pop", "HipHop", "Jazz", "Blues", "Classic", "Country", "Electronic", "Musical Theatre" };

genre from_string_to_genre(const string& genreStr) {
    if (genreStr == "Rock") return rock;
    if (genreStr == "Pop") return pop;
    if (genreStr == "Jazz") return jazz;
    if (genreStr == "Classic") return classic;
    if (genreStr == "Hiphop") return hiphop;
    if (genreStr == "Blues") return blues;
    if (genreStr == "Country") return country;
    if (genreStr == "Electronic") return electronic;
    if (genreStr == "Musical Theatre") return musical_theatre;
    return pop; // default genre
}

void displayGenres() {
    cout << endl << "Available Genres: " << endl;
    for (const string& genre : GenreNames) {
        cout << genre << endl;
    }
}
vector<string> Artists;
void displayArtists() {
    if (Artists.empty()) {
        cout << endl << "Sorry! No Available Artists At The Moment.";
    }
    else {
        cout << endl << "Available Artists: " << endl;
        for (int i = 0; i < Artists.size(); i++)
            cout << Artists[i] << endl;
    }
}

void printAlbum(const album& album) {
    cout << "Album Name: " << album.album_name << endl;
    cout << "Artist Name: " << album.artist_name << endl;
    cout << "Genre: " << GenreNames[album.genre] << endl;
    cout << "Price: " << album.price << endl;
}

void printOrder(const order& order) {
    cout << "Order ID: #" << order.order_ID << endl
        << "Customer ID: #" << order.customer_ID << endl
        << "Total Amount: $" << order.total << endl
        << "Delivery Address: " << order.address << endl;

    for (int i = 0; i < order.item_count; ++i) {
        const ordered_album& item = order.items[i];
        cout << "Album: " << item.name << endl
            << "Quantity: " << item.quantity << endl
            << "Price per item: $" << item.price << endl
            << "Total: $" << item.quantity * item.price << endl;
    }
}

void displayAlbumsByArtist(const string& artistName, const album albums[], int album_count) {
    cout << endl << "Albums By Artist " << artistName << ":" << endl << endl;
    for (int i = 0; i < album_count; i++) {
        if (albums[i].artist_name == artistName) {
            printAlbum(albums[i]);
        }
    }
}

void displayAlbumsByGenre(genre genre, const album albums[], int album_count) {
    cout << endl <<"Albums In Genre " << GenreNames[genre] << ":" << endl << endl;
    for (int i = 0; i < album_count; i++) {
        if (albums[i].genre == genre) {
            printAlbum(albums[i]);
        }
    }
}

const int max_album = 100;
int album_count = 0;
album albums[max_album];
int current_order_id = 54632892;
int current_customer_id = 849379279;

void addAlbum(const string& albumname, const string& artistname, genre music_genre, double price) {  //admin
    if (album_count >= max_album) {
        cout << "Album Storage is Full" << endl;
        return;
    }
    albums[album_count++] = { albumname, artistname, music_genre, price };
    bool flag = false;
    for (int i = 0; i < (Artists.size()); i++) {
        if (artistname == Artists[i]) {
            flag = true;
        }
    }
    if (flag == false) {
        Artists.push_back(artistname);
    }
}

void totalAmount(order& ord) {
    double total = 0.0;
    for (int i = 0; i <= ord.item_count; i++) {
        total += ord.items[i].quantity * ord.items[i].price;
    }
    ord.total = total;
}

void addToCart(order& ord, const string& album_name, int quantity) {
    if (ord.item_count >= 10) {
        cout << "Your cart is full, cannot add more items." << endl;
        return;
    }

    double price = 0.0;
    bool found = false;
    for (int i = 0; i <= album_count; i++) {
        if (albums[i].album_name == album_name) {
            price = albums[i].price;
            found = true;
            break;
        }
    }

    if (!found) {
        cout << "Album not found. Please Try Again." << endl;
        return;
    }

    for (int i = 0; i < ord.item_count; i++) {
        if (album_name == ord.items[i].name) {
            char confirm;
            cout << "The album" << album_name << " is already in cart,do you want to add  more? ";
            cout << "press 'y' to confirm and any other key to cancel";
            cin >> confirm;
            if (confirm == 'y' || confirm == 'Y') {
                ord.items[i].quantity += quantity;
                totalAmount(ord);
                cout << "The album " << album_name << " was added to the cart successfully!" << endl << endl;
            }
            else {
                cout << "The album " << album_name << " was not added to the cart." << endl << endl;
            }
            return;
        }
    }

    ord.items[ord.item_count++] = { album_name, quantity,price };
    totalAmount(ord);
    cout << "The album " << album_name << " was added to cart successfully!" << endl << endl;
}

void removeFromCart(order& ord, const string& album_name) {
    int index = -1;
    for (int i = 0; i <= ord.item_count; i++) {
        if (ord.items[i].name == album_name) {
            index = i;
            break;
        }
    }

    if (index == -1) {
        cout << "Album not found in cart." << endl;
        return;
    }

    for (int i = index; i < ord.item_count - 1; i++) {
        ord.items[i] = ord.items[i + 1];
    }
    ord.item_count--;
    totalAmount(ord);
    cout << "The album " << album_name << " was removed from cart successfully." << endl;
}

void createAndDisplayOrder() {
    order ord;
    ord.order_ID = current_order_id++;
    ord.customer_ID = current_customer_id++;
    ord.total = 0.0;
    ord.item_count = 0;

    cout << "Enter Delivery Address: ";
    cin.ignore();
    getline(cin, ord.address);

    int item_count;
    cout << "Enter Number of Albums: ";
    cin >> item_count;

    for (int i = 0; i < item_count; i++) {
        string albumName;
        int quantity;

        cout << "Enter Album Name: ";
        cin.ignore();
        getline(cin, albumName);

        cout << "Enter Quantity: ";
        cin >> quantity;

        addToCart(ord, albumName, quantity);
    }
    cout << "- - - - - ORDER RECEIVED - - - - -" << endl << endl;
    cout << "ORDER SUMMARY: " << endl << endl;

    printOrder(ord);
}
void displayCart(const order & ord) {
    if (ord.item_count == 0) {
        cout << "Your Cart is Empty" << endl;
        return;
    }
    cout <<"Current Cart:" << endl << endl;
    for (int i = 0; i < ord.item_count; ++i) {
        const ordered_album& item = ord.items[i];
        cout << "Album: " << item.name << endl
            << "Quantity: " << item.quantity << endl
            << "Price per item: $" << item.price << endl
            << "Total: $" << item.quantity * item.price << endl;
    }
    cout << "Total Amount: $" << ord.total << endl;
}

void management_menu() {
    int choice;
    do {
        cout << endl << "                    MUSIC STORE MANAGEMENT SYSTEM     " << endl << endl;
        cout << "1. Display Music Genres\n";
        cout << "2. Display Artists\n";
        cout << "3. Display Albums By Genre\n";
        cout << "4. Display Albums By Artist\n";
        cout << "5. Add Album\n";
        cout << "6. Exit\n";
        cout << endl << "Choice: ";
        cin >> choice;
        switch (choice) {
        case 1:
            displayGenres();
            break;
            
            
        case 2:
            displayArtists();
            break;
            
        case 3: {
            string genre_string;
            cout << "Enter Genre (Rock, Pop, Hiphop, Jazz, Blues, Classic, Country, Electronic, Musical Theatre): ";
            cin >> genre_string;
            genre genre = from_string_to_genre(genre_string);
            displayAlbumsByGenre(genre, albums, album_count);
            cout << endl;
            break;
        }

        case 4: {
            string artist_name;
            cout << "Enter Name of Artist: ";
            cin.ignore();
            getline(cin, artist_name);
            displayAlbumsByArtist(artist_name, albums, album_count);
            cout << endl;
            break;
        }
        case 5: {
            char add_more;
            do {
                string album_name, album_artist, str_genre;
                double price;
                cout << endl << "                            ALBUM DATABASE     " << endl << endl;
                cout << "Enter Album Name: ";
                cin.ignore();
                getline(cin, album_name);
                cout << "Enter Artist Name: ";
                getline(cin, album_artist);
                cout << "Enter Genre (Rock, Pop, HipHop, Jazz, Blues, Classic, Country, Electronic, Musical Theatre): ";
                cin >> str_genre;
                genre genre = from_string_to_genre(str_genre);
                cout << "Enter Price: ";
                cin >> price;
                addAlbum(album_name, album_artist, genre, price);
                cout << "The album " << album_name << " has been successfully added to the system! " << endl;

                cout << "Do you want to add another album? (y/n): ";
                cin >> add_more;
                cout << endl;
            } while (add_more == 'y' || add_more == 'Y');
            break;
        }
        case 6:
            cout << "Exiting the program. " << endl;
            break;

        default:
            cout << "Invalid Input. Please Try Again" << endl;
            break;
        
    }
    
    } while (choice != 6);
}
void admin_menu() {
    string admin_pass;
    cout << "Enter Admin Password: " << endl;
    cin >> admin_pass;

    if (admin_pass != "admin") {
        cout << "Incorrect Password. Please Try Again!" << endl;
        return;
    }
    management_menu();

}

void user_menu() {
    order current_order;
    int choice;
    do {
        cout << "\n                             MUSIC STORE       \n" << endl << endl;
        cout << "1. Display Music Genres\n";
        cout << "2. Display Artists\n";
        cout << "3. Display Albums By Genre\n";
        cout << "4. Display Albums By Artist\n";
        cout << "5. Create & Display Order\n";
        cout << "6. Add To Cart\n";
        cout << "7. Remove From Cart\n";
        cout << "8. Display Cart\n";
        cout << "9. Exit\n" << endl << endl;
        cout << "Enter Your Choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            displayGenres();
            break;
        case 2:
            displayArtists();
            break;
        case 3: {
            string genre_string;
            cout << "Enter Genre (Rock, Pop, Hiphop, Jazz, Blues, Classic, Country, Electronic, Musical Theatre): ";
            cin >> genre_string;
            genre genre = from_string_to_genre(genre_string);
            displayAlbumsByGenre(genre, albums, album_count);
            break;
        }

        case 4: {
            string artist_name;
            cout << "Enter Name of Artist: ";
            cin.ignore();
            getline(cin, artist_name);
            displayAlbumsByArtist(artist_name, albums, album_count);
            break;
        }

        case 5:
            createAndDisplayOrder();
            break;
            


        case 6: {
            string albumName;
            int quantity;
            cout << "Enter Album Name to Add to Cart: ";
            cin.ignore();
            getline(cin, albumName);
            cout << "Enter Quantity: ";
            cin >> quantity;
            addToCart(current_order, albumName, quantity);
            break;
        }

        case 7: {
            string albumName;
            cout << "Enter Album Name to Remove from Cart: ";
            cin.ignore();
            getline(cin, albumName);
            removeFromCart(current_order, albumName);
            break;
        }
        case 8: {
                displayCart(current_order);
                break;
            }
        case 9:
            cout << "Exiting the program." << endl;
            break;

        default:
            cout << "Invalid Input. Please Try Again" << endl;
        }
    } while (choice != 9);
}
void menu() {
    string user_res;
    cout<<"                             MUSIC STORE                              "<<endl;
    cout << "                    Are You an Admin or a User?                     " << endl;
    cin >> user_res;
    if (user_res == "admin" || user_res == "Admin" || user_res == "ADMIN") {
        admin_menu();
    }
    else if (user_res == "user" || user_res == "User" || user_res == "USER") {
        user_menu();
    }
    else {
        cout << "Invalid Input. Please Try Again " << endl;
        menu();
    }
}
int main() {

    addAlbum("The Dark Side of the Moon","Pink Floyd", rock, 330.45);
    addAlbum("Younis", "Mohamed Mounir", rock, 320.5);
    addAlbum("180 Daraga", "Tamer Hosny", pop, 180.49);
    addAlbum("Kol Ma A2ool Ah", "Sherine", pop, 130.49);
    addAlbum("Kind of Blue","Miles Davis",jazz,250);
    addAlbum("A Love Supreme","John Coltrane",jazz,120.68);
    addAlbum("Donda","Kanye West",hiphop,540);
    addAlbum("Because the Internet","Donald Glover",hiphop,430.5);
    addAlbum("At Folsom Prison","Johnny Cash",country,240);
    addAlbum("Red Headed Stranger","Willie Nelson",country,550.32);
    addAlbum("Live at The Regal","B.B King",blues,230);
    addAlbum("Blues","Jimi Hendrix",blues,250);
    addAlbum("Bach: The Goldberg Variations","Glenn Gould",classic,650);
    addAlbum("The 50 Greatest Pieces of Classical Music","The London Philharmonic Orchestra",classic,700);
    addAlbum("Discovery","Daft Punk",electronic,350);
    addAlbum("Play","Moby",electronic,240);
    addAlbum("Wicked","Various Artists",musical_theatre,270);
    addAlbum("Anything Goes","John McGlinn",musical_theatre,300);



    menu();
    return 0;
}


