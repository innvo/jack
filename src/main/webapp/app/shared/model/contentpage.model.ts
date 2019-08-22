export interface IContentpage {
  id?: number;
  title?: string;
  content?: string;
}

export class Contentpage implements IContentpage {
  constructor(public id?: number, public title?: string, public content?: string) {}
}
