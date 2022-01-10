import { Member } from "./member.model";

export class Message {
    messageId: BigInteger | undefined;
    author: Member | undefined;
    creationDate: Date = new Date(0);
    content: String | undefined;
    codeOutput: String | undefined;
}
