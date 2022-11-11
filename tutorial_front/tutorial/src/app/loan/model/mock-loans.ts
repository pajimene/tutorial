import { LoanPage } from "./LoanPage"
import { CLIENT_DATA } from "src/app/client/model/mock-clients"
import { GAME_DATA } from "src/app/game/model/mock-games"

export const LOAN_DATA: LoanPage = {
    content: [
        { id: 1, client: CLIENT_DATA[0], game: GAME_DATA[0], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 2, client: CLIENT_DATA[1], game: GAME_DATA[1], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 3, client: CLIENT_DATA[2], game: GAME_DATA[2], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 4, client: CLIENT_DATA[3], game: GAME_DATA[3], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 5, client: CLIENT_DATA[4], game: GAME_DATA[4], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 6, client: CLIENT_DATA[5], game: GAME_DATA[5], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
        { id: 7, client: CLIENT_DATA[6], game: GAME_DATA[6], initialDate: new Date('02/11/2022'), finalDate: new Date('12/11/2022')},
    ], 
    pageable : {
        pageSize: 5,
        pageNumber: 0,
        sort: [
            {property: "id", direction: "ASC"}
        ]
    },
    totalElements: 7
}
