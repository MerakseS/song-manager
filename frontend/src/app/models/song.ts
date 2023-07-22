import { Album } from "./album";
import { Artist } from "./artist";

export class Song {
    id: string;
    name: string;
    link: string;
    duration: number;
    album: Album;
    artists: Artist[];
}
