export interface IGalaxy {
  id?: number;
  name?: string;
  type?: string;
}

export class Galaxy implements IGalaxy {
  constructor(public id?: number, public name?: string, public type?: string) {}
}
